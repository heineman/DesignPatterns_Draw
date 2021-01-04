The following are the requirements for the Draw application. All requirements are satisfied by the **draw.5** tag.

## Create Element
1.	A user can create elements (such as rectangles, ovals) in a drawing canvas.
    1.	After creating an element, all other selected elements are de-selected, the select tool is chosen, and the newly created element is selected.

## Select Element(s)
2.	With select tool chosen, an element can be selected; if the shift key is held down while pressing the mouse then multiple elements can be selected.
    1.	Only “top-level” elements can be selected; it is not possible to individually select an element that is part of a larger group.
    2.	If shift key is down and mouse pressed on a selected element, it is unselected.

## Move Element(s)
3.	A selected element can be moved; if multiple elements are selected, they all move relative to the one on which the user initially pressed the mouse.

## Resize Element
4.	A selected element can be resized by dragging an anchor; if multiple elements are selected when a resize occurs, then only the one on which the user pressed the mouse will be resized and all other selected elements will be de-selected.
    1.	If a group of elements is resized, all constituent elements are resized proportional to the grouped element’s bounding box.

## Delete Element(s)
5.	A selected element can be deleted; if multiple elements are selected, they are all deleted.

## Group and Ungroup Elements
6.	Two or more selected elements can be grouped into a composite element.
    1.	Grouped elements can be included as part of a group.
7.	A grouped element can be un-grouped and all constituent elements become top-level elements.
    2.	When a grouped element has elements that are themselves groups, they remain as groups and are unaffected.

## Duplicate Element(s)
8.	All selected elements can be duplicated.
    1.	Duplicated elements appear offset (+20, +20) to original elements.

## Cut, Copy, and Paste Elements
9.	All selected elements can be copied.
10.	All selected elements can be cut; they are removed from the canvas.
11.	If elements are cut and placed on the clipboard, they can be pasted to the canvas
    1.	If elements have been copied and placed on the clipboard, when pasted they are offset by (+20, +20) similar to duplication.

## Save and Load Application State
12.	The user can save the state of the canvas to a file on disk.
13.	The user can load the state of the canvas from a file on disk.
14.	The user can request a new canvas which deletes all existing elements.
