---
layout: page
title: Zijun's Project Portfolio Page
---

## Project: McScheduler

McScheduler is a one-stop solution for McDonald's Shift Managers  to manage shift scheduling and worker contact/compensation. 
The user interacts with it using a CLI, and it has a GUI created with JavaFX. It is written in Java, and has about 10 kLoC.

Given below are my contributions to the project.

* **New Feature**: Added base implementation and functionality for shifts, including editing and listing shifts.
  * _What it does_: Allows the user edit existing shifts and view all shifts. This feature also serves as a base for all shift-related operations within the McScheduler.
  * _Justification_: Shifts are an important feature for shift managers to work with and is part of the core functionality of McScheduler.
  * _Highlights_: Shifts are added very similarly to workers (e.g. use of `EditShiftDescriptor`) to maintain consistency within the code.
    The work relating to the implementation of shifts involves most parts of the application, including storage, GUI, command logic and model representation.
  * _Featured PRs_: [\#35](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/35)
  
* **New Feature**: Added leaves and the functionality for adding, mass-adding, cancelling and mass-cancelling leaves.
  * _What it does_: allow users to assign workers to leave or remove them from leave, either as a single assignment (1 worker to 1 shift type for leave)
  or as a range of dates (1 worker, a start day/time and an end day/time).
  * _Justification_: Leaves are an important feature for shift managers to use to keep track of their worker and avoid assigning individuals on leave
  to shifts. As workers typically take leave over a range of dates, the mass operations for leave are added to allow convenient leave-related operations.
  * _Highlights_: 
    * This implementation makes use of existing `assign` and `reassign` commands as they function similarly. This heavily reduces code duplication since `take-leave`
    is essentially a wrapper for those two commands. 
    * To further increase the organic feel of `mass-take-leave` command, placeholder shifts are automatically added and to all day/times within the range specified.
    This change makes it much more convenient for managers to handle leaves as they are typically taken in real life.
  * _Credits_: Reuse of assign-related commands since these are vastly similar.
  * _Featured PRs_: [\#100](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/100)

* **Code contributed**: [RepoSense link](https://nus-cs2103-ay2021s1.github.io/tp-dashboard/#breakdown=true&search=F10-4&sort=groupTitle&sortWithin=title&since=2020-08-14&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&tabOpen=true&tabType=authorship&tabAuthor=WangZijun97&tabRepo=AY2021S1-CS2103-F10-4%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code)

* **Enhancements to existing features**:
  * Wrote extensive tests for the implementation of shift which greatly increased coverage [\#50](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/50)
  
* **Bug Fixes**:
  * Ensure single representation of shifts when loading from save [\#241](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/241)
  * Ensure single representation of shifts and synchronization between assignments and shifts [\#199](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/199), 
    [\#241](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/241)

* **Documentation**:
  * User Guide:
    * Add documentation for leave-related functions: [\#100](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/100), 
    [\#127](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/127)
    * Did cosmetic tweaks to existing documentation of features: [\#51](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/51), 
    [\#244](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/244)
  * Developer Guide:
    * Added implementation details for shift and leave [\#100](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/100), 
      [\#118](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/118)

* **Community**:
  * PRs reviewed (with non-trivial review comments): [\#42](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/42), 
  [\#84](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/84), [\#98](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/98), 
  [\#194](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/194), [\#198](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/198)
  * Discussed implementation of other features with other developers within group: [\#88](https://github.com/AY2021S1-CS2103-F10-4/tp/issues/89), 
  [\#89](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/89)
  * Reported bugs to other groups: [PE-D](https://github.com/wangzijun97/ped/issues)

