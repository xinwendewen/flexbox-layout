# Introduction

This project is derivied from the google FlexboxLayout with the following changes:

- add a new stand-alone java library that implements the CSS Flexbox algorithm

- take reference and rewrite most of the original flexbox algorithm according to my own understanding of implementing flexbox specification on android platform

- remove the code related to RecyclerView, the optimization during flex calculation, the android tests and the cat gallery demo

- delegate the flex calculation during the measure and layout process of google FlexboxLayout to the library. Verify the correctness by existing unit tests and the playground demo

- some flexbox features are not implemented yet such as order and baseline alignment

# Algorithm

## The Measure Phase

### 1.generate the flex lines data structure

given the

- flex items

- measure request of the container

- container flex properties

measure all flex items and fill the flex lines with flex items

```
prepare flex lines
prepare current flex line
for each flex item {
    measure its main size (do not take filled flex items into account)
    measure its cross size (take occupied cross size into account)
    clamp by min/max constrains and remeasure if needed
    if (isWrapNeeded) {
        finish and append current flex line
        remeasure if cross size MATCH_PARENT
        prepare new current flex line
    }
    add current item to current flex line
}
append current flex line
return flex lines

boolean isWrapNeeded(flexContainer, currentFlexLine, flexItem) {
    if (flexContainer.flexWrap == noWrap) return false;
    if (flexContainer.mainAxisMeasureRequest.isUnconstrained) return false;
    return (flexContainer.mainAxisMeasureRequest.initialSize <
        currentFlexLine.mainSize + flexItem.outerMainSize)
}
```

### 2.determine the main size of the container

given

- the generated flex lines

- measure request of the container

calculate and determine the main size of the container, as one input for flexable length calculation. if the container requires exact size, respect that, otherwise use the min of require size and the max main size of flex line in generated flex lines

```
int determineMainSize() {
    int largestFlexLineMainSize = flexLines.getLargetsMainSize
    int requestedMainSize = mainAxisMeasureRequest.getSize
    int finalMainSize
    if container.isFixedSize:
        finalMainSize = requestedMainSize
    else:
        // not tight
        finalMainSize = minOf(largestFlexLineMainSize, requestedMainSize)
    return finalMainSize
}
```

### 3.calculate the flexible lengths for each flex line

given

- the flex lines data structure

- the determined main size of the container

- measure request of the container

shrink or expand flex items in every flex line inorder to fill the available space(may be negative) of every flex line, and the updated flex lines date structure.

flexible lengths are calculated within each flex line independently

```
for each flexLine in flexLines:
    calculate flexible lengths of current flexLine
```

#### the frozen item

a frozen item means it can no longer shrink or expend

a flex is frozen when

- it has a flex factor of zero

- it has exceeds its dimension boundary constraints (min/max) after shrink or expand

#### flexible length calculation

- calculate the available space

     - grow when positive

     - shrink when negative

- calculate the space unit size

- for each item

     - grow or shrink according to its flex factor

     - check min/max constraint

     - remeasure

     - update flex line state

#### flexible length calculation may occur multiple times

if some item violates and clamps itself to the min or max constraint after shrink or expand calculation, the available space is not fully distributed, we have to calculate the flexible again untill the main size of the flexline equals to the main size of the conatiner

#### rounding error accumulation and compensation

##### the problem

when you distribute available space to flex items, the distributed size is of type float, but the android view system accept integer pixels only, so rounding is required, along with deminsion errors.

for example, if a flex line has 100 flex items, after the flexible lengths are calculated, every flex item has a main size of 10.4 pixels, which will be 10 pixels after rounding, then the total difference of main size of this flex line will be 40 pixels.

##### the solution

an error accumulation and compensation strategy, by accumulating the rounding error while calculate the flexible length for each flex item

- if the accumulated error is greater than 1 pixel, add 1 pixel to current item, and decrease the accumulated error sum by 1

- if the accumulated error is less than -1 pixel, minus 1 pixel to current item, and increase the accumulated error sum by 1

#### pseudo code

```
while (flexLine.isNotFrozen && flexLine.mainSize != containerMainSize) {
    int available = containerMainSize - flexLine.mainSize
    float unit = available / flexLine.flexFactorSum
    boolean hasViolation = false
    for each item in flexLine.items:
        if item.isFrozen || item.hasBeenClamped:
            continue
        int measuredMainSize = item.getMeasuredMainSize
        int newMainSize = measuredMainSize - item.flexFactor * unit
        if (newMainSize < item.minMainSize):
            hasViolation = true
            newMainSize = item.minMainSize
            flexLine.flexFactorSum -= item.flexFactor
            item.markFrozen
        } else if (newMainSize > item.maxMainSize) {
            hasViolation = true
            newMainSize = item.maxMainSize
            flexLine.flexFactorSum -= item.flexFactor
            item.markFrozen
        }
        item.remeasureWidthFixedMainSize(containerMeasureRequests,
            newMainSize, flexLine.crossSizeBrfore)
        item.clampByMinMax
        update the flex line state
        if (hasViolation) {
            break;
        }
}
```

### 4.determine the cross size of the container

given

- the cross axis measure request of the conatiner

- the flex lines data structure

calculate the cross size of the container, which determines whether cross axis alignment is required.

if the container requires fixed cross size and the container itself has multiple flex lines, we may need to perform cross axis alignment to flex lines within the container, otherwise the flex container just wrap flex lines.

```
if crossAxisMeasureRequest require fixed size:
    return crossAxisMeasureRequest.getRequireSize
else
    return flexLines.crossSize + containerCrossAxisPadding
```

set cross size for single-line flex container

```
if crossAxisMeasureRequest require fixed size && flexLines.size() == 1:
    flexLine.get(0).crossSize = conatainerCrossSize - containerCrossPaddings
```

### 5.align flex lines along the cross axis

given

- flex lines

- cross size of the container

- align-content

perform flex lines alignment according to the align-content property.

#### check whether alignment is required

if the cross size of the container is unspecified, it just wrap the flex lines, so no room for alignment. if there is only one flex line, no need for alignment neither.

when the cross size of the container is specified, flex lines are aligned according to property align-content.

```
boolean needAlignment(containerCrossAxisMeasureRequest, flexLines):
    if (flexLines.size == 1) -> false
    if (!containerCrossAxisMeasureRequest.requireExactSize) -> false
    return true
```

#### perform alignment by align-content

to perform flex line alignment along the cross axis, we calculate the free space, and distribute the free space to existing flex lines or new dummy flex lines used as placeholders

##### flex-start

do nothing, flex lines satisfied flex-start by nature

##### flex-end

calculate the free space

- if positive, add a dummy spacee flex line to the top of the flex lines

- if negative, overflow the top flex line

```
freeSpace = containerCrossSize - flexLines.crossSize
flexLines.addTop(DummyFlexLine.withCrossSize(freeSpace))
```

> note that add a dummy flex line with negative cross size is equivalent to offsetting next real flex line backward along the cross axis, so that it can be overflowed

##### stretch

calculate the free space

- if positive, distribute to each flex line

- if negative, do nothing

```
freeSpace = containerCrossSize - flexLines.crossSize
if (freeSpace > 0) {
    unitSpace = freeSpace / flexLines.count
    for each flexLine in flexLines {
        flexLine += unitSpace
    }
}
```

##### space-around

calculate the free space

- if positive, calculate the cross size of dummy flex line, and add a dummy flex line to the top and bottom of every flex line

- if negative, treat it as align-content center

```
freeSpace = containerCrossSize - flexLines.crossSize
if (freeSpace > 0) {
    unitSpace = freeSpace / (flexLines.count * 2)
    for each flexLine in flexLines {
        flexLine.insertAbove(DummyFlexLine.withCrossSize(unitSpace)
        flexLine.insertAfter(DummyFlexLine.withCrossSize(unitSpace)
    }
} else {
    // see align-content center
}
```

##### space-between

calculate the free space

- if positive, calculate the space between two flex lines, add a dummy flex line between every two flex lines

- if negative, do nothing

```
freeSpace = containerCrossSize - flexLines.crossSize
if (freeSpace > 0) {
    unitSpace = freeSpace / (flexLines.count - 1)
    for each flexLine in flexLines {
        if (flexLine is the first one) {
            continue; // skip the first flex line
        }
        flexLine.insertAbove(DummyFlexLine.withCrossSize(unitSpace)
    }
}
```

##### center

calculate the free space

- if positive, add two dummy space flex line with cross size equals to (free space / 2) to the top and bottom of the flex lines

- if negative, overflow the top and bottom flex lines equally in both directions

```
freeSpace = containerCrossSize - flexLines.crossSize
unitSpace = freeSpace / 2
flexLines.addTop(DummyFlexLine.withCrossSize(unisSpace))
flexLines.addBotom(DummyFlexLine.withCrossSize(unisSpace))
```

### 6.stretch items along the cross axis

since the cross size of every flex line has been determined, it is time to stretch flex items within a flex line if required

given

- flex lines structure

- align-items and align-self property

we iterate every flex item in every flex line, if the flex item require stretch, then stretch it. then return the flex lines structure after stretched.

#### check whether a flex item need stretch

```
boolean needStretch(flexItem, containerAlignItems, flexLine) {
    if (flexItem.outerCrossSize >= flexLine.crossSize) {
        return false;
    }
    if (flexItem.alignSelf is stretch) {
        return true
    }
    if (flexItem.alignSelf is auto && container.alignItems is stretch) {
        return true
    }
    return false
}
```

#### stretch an item

remeasure it with specified cross size after stretched and specified current main size of the item

```
stretch(flexItem, flexLine) {
    newCrossSize = flexLine.crossSize - flexItem.crossAxisMargin
    newCrossSize = clamp(flexItem.minCrossSize, flexItem.maxCrossSize)
    item.fixedSizeMeasure(item.mainSize, newCrossSize)
}
```
## The Layout Phase

### how to layout an item in general

#### the main idea

to layout an item, given the layout area of the container, i.e, the inner area of the container, calculate the left top right bottom position relative to the layout area

#### two anchors

an anchor is the start layout position for an item in one direction, vertically or horizontally, you calculate the position of an item start from the anchor position

once a item's layout position is determined, the anchor is forwarded and prepared for next item

#### layout direction

- horizontally

layout from left to right by default, from right to left if reversed

- vertically

layout from top to bottom by default, from bottom to top if reversed

#### layout process

1. detemine the LTRB position

given the horizontal and vertical layout anchor, calculate the start coordinates for each direction, then calculate the end coordinates for each direction according to the dimension of the item

```
left
top
right
bottom

if (horitonalAnchor.isReversed) {
    right = horitonalAnchor.value - item.rightMargin
    left = right - item.width
} else {
    left = horitonalAnchor.value + item.leftMargin
    right = left + item.width
}

if (verticalAnchor.isReversed) {
    bottom = verticalAnchor.value - item.bottomMargin
    top = bottom - item.height
} eles {
    top = verticalAnchor.value + item.topMargin
    bottom = top + item.height
}
```

2. prepare anchors for next item

```
if (horitonalAnchor.isReversed) {
    horitonalAnchor.value -= (item.width + item.leftMargin)
    horitonalAnchor.value -= spaceBetweenItems
} else {
    horitonalAnchor.value += (item.width + item.rightMargin)
    horitonalAnchor.value += spaceBetweenItems
}

if (verticalAnchor.isReversed) {
    verticalAnchor.value -= (item.height + item.topMargin)
    verticalAnchor.value -= spaceBetweenItems
} else {
    verticalAnchor.value += (item.height + item.bottomMargin)
    verticalAnchor.value += spaceBetweenItems
}
```

### flexbox style layout process

#### 1.resolve the flex layout direction

the main axis or cross axis is from left to right or from top to bottom

we determine whether the layout direction should be reversed according to the flexDirection, flexWrap and the LTR/RTL propertes

```
boolean mainAxisReversed = false
boolean crossAxisReversed = false

when flexDirection:
    row:
        if (RTL) {
            mainAxisReversed = true
        }
        if (flexRrap = wrap-reverse) {
            crossAxisReversed = true
        }
    row-reverse:
        if (LTR) {
            mainAxisReversed = true
        }
        if (flexRrap = wrap-reverse) {
            crossAxisReversed = true
        }
    column:
        if (LTR && flexWrap = wrap-reverse) {
            crossAxisReversed = true
        }
        if (RTL && flexWrap = wrap) {
            crossAxisReversed = true
        }
    column-reverse:
        if (LTR && flexWrap = wrap-reverse) {
            crossAxisReversed = true
        }
        if (RTL && flexWrap = wrap) {
            crossAxisReversed = true
        }
        mainAxisReversed = true
```

#### 2.determine the main axis anchor position and the space between items

given the

- layout direction

- justify content

- inner main size of container

- main size of flex line

calculate the main axis anchor position and the space between two flex items if exists.

##### calculation according to property justify-content

- flex-start

```
anchor = reversed ? containerInnerMainSize : 0

```

- flex-end

```
anchor = reversed ? flexLineMainSize : containerInnerMainSize - flexLineMainSize
```

- center

```
anchor = reversed ? (containerInnerMainSize + flexLineMainSize) / 2 : (containerInnerMainSize - flexLineMainSize) / 2
```

- space-around

```
spaceBetween = (containerMainSize - flexLineMainSize) / itemCount
anchor = reversed ? containerInnerMainSize - spaceBetween / 2 : spaceBetween / 2

```

- space-between

```
spaceBetween = itemCount > 1 ? (containerInnerMainSize - flexLineMainSize) / (itemCount - 1) : 0
anchor = reversed ? containerInnerMainSize : 0
```

- space-evenly

```
spaceBetween = (containerInnerMainSize - flexLineMainSize) / (itemCount + 1)
anchor = reversed ? containerInnerMainSize - spaceBetween : spaceBetween
```

#### 3.determine the cross axis anchor position

given the inner cross size of the container, determin the cross axis anchor position

```
crossAnchor = isReversed ? containerCrossSize: 0
for each flexLine in FlexLines {
    layoutFlexLine(flexLine, crossAnchor)
    if (isReversed) {
        crossAnchor -= flexLine.crossSize
    } else {
        crossAnchor += flexLine.crossSize
    }
}
```

#### 4.layout a single flex item

given

- isMainAxisReversed and isCrossAxisReversed

- mainAxisAnchor and crossAxisAnchor

- the flex item

instead of calculating the LTRB position, you calculate the

- mainStart
- mainEnd
- crossStart
- crossEnd

of and flex item, taking the alignItems and alignSelf properties into accnout, and then layout the flex item.

##### 4.1 calculate the mainStart and mainEnd

```
if (isMainAxisReversed) {
    mainEnd = mainAxisAnchor - item.mainEndMargin
    mainStart = mainEnd - item.mainSize
} else {
    mainStart = mainAxisAnchor + item.mainStartMargin
    mainEnd = mainStart + item.mainSize
}
```

##### 4.2 resolve the cross alignment property

```
if (alignSelf != AUTO) {
    use alignSelf
} else {
    use alignItems
}
```

##### 4.3 align and calculate the crossStart and crossEnd

```
switch (alignSelf) {
    case stretch // we have stretched this item during the measure phase, so do nothing here
    case flex-start
        if (isCrossAxisReversed) {
            crossEnd = crossAnchor - crossEndMargin
            crossStart = crossEnd - item.crossSize
        } else {
            crossStart = crossAnchor + crossStartMargin
            crossEnd = crossStart + item.crossSize
        }
    case flex-end
        if (isCrossAxisReversed) {
            crossStart = crossAnchor - flexLineCrossSize + item.crossStartMargin
            crossEnd = crossStart + item.crossSize
        } else {
            crossEnd = crossAnchor + flexLineCrossSize - item.crossEndMargin
            crossStart = crossEnd - item.crossSize
        }
    case center
        if (isCrossAxisReversed) {
            crossEnd = crossAnchor - (flexLineCrossSize / 2 - item.outerCrossSize / 2) - item.crossEndMargin
            crossStart = crossEnd - item.crossSize
        } else {
            crossStart = crossAnchor + (flexLineCrossSize / 2 - item.crossSize / 2) + item.crossStartMargin
            crossEnd = crossStart + item.crossSize
        }
}
```

##### 4.4 layout the item

```
item.layout(mainStart, mainEnd, crossSize, crossEnd)
```

#### 5.prepare main axis anchor for next item

```
if (isMainAxisReversed) {
    mainAxisAnchor -= (item.mainStartMargin + itemMainSize + spaceBetweenItems)
} else {
    mainAxisAnchor += (item.mainEndMargin + spaceBetweenItems)
}
```

#### 6.prepare cross axis anchor for next flex line

```
if (isCrossAxisReversed) {
    crossAxisAnchor -= flexLine.crossSize
} else {
    crossAxisAnchor += flexLine.crossSize
}
```

### the overall algorithm of the layout phase

```
isMainAxisReversed, isCrossAxisReversed :=
resolveFlexLayoutDirection(flexDirection, flexWrap, isRTL)

for each flexLine in flexLines {
    mainAxisAnchor, spaceBetweenItems := calculateMainAxisAnchorPosition(isMainAxisReversed, containerInnerMainSize, flexLine)
    crossAxisAnchor = isCrossAxisReversed ? containerInnerCrossSize : 0
    for each item in flexLine {
        layoutItem(mainAxisAnchor, isMainAxisReversed, crossAxisAnchor, isCrossAxisReversed, flexLine)
        forwardMainAxisAnchor(mainAxisAnchor, isMainAxisReversed, item, spaceBetweenItems)
    }
    forwardCrossAxisAnchor(crossAxisAnchor, isCrossAxisReversed, flexLine)
}
```
