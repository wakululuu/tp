---
layout: page
title: Foo Kai En's Project Portfolio Page
---

## Project: McScheduler

Developed during a Software Engineering introductory module, the McScheduler is a desktop application, targeted at
McDonald's managers for easy shift and worker management. The McScheduler is optimized for use via a CLI, while still
having benefits of a GUI, created with JavaFX. This project is written in Java in OOP style, and has about 10 kLoC.

Given below are my contributions to the project.

* **New Feature**: Added the ability to maintain a master list of valid roles in the system
  * _What it does:_
    * Allows the user to add, edit and delete roles which can be taken on by workers in a McDonald's outlet
    * Ensures that any role added to the list of roles that a worker is able to take on is a valid role in the system
    * Ensures that any role added to the list of roles that need to be filled on a shift is a valid role in the system 
  * _Justification:_
    * Automates the process of renaming or deleting all instances of a role from the system with a single command
    * Ensures the consistent naming of a role throughout the system
    * Alerts the user when there is a typo 
  * _Highlights:_ _(to be edited)_

* **New Feature**: Added the ability to assign a worker to a shift _(to be edited)_

* **Enhancements to existing features**:
  * Revamped the GUI (PR [\#193]())

* **Code contributed**: [RepoSense](https://nus-cs2103-ay2021s1.github.io/tp-dashboard/#breakdown=true&search=f10-4&sort=groupTitle&sortWithin=title&since=2020-08-14&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&tabOpen=true&tabType=authorship&tabAuthor=wakululuu&tabRepo=AY2021S1-CS2103-F10-4%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code~other)

* **Documentation**:
  * User Guide:
    * Added documentation for the [`assign`/`unassign` feature](../UserGuide.md#assigning-a-worker-to-a-role-in-a-shift-assign),
      as well as the [`role-add`/`role-edit`/`role-delete` feature](../UserGuide.md#adding-a-role-role-add)
  * Developer Guide:
    * Added implementation details of the [`assign`/`unassign` feature](../DeveloperGuide.md#assignunassign-feature)

* **Community**:
  * [PRs reviewed](https://github.com/AY2021S1-CS2103-F10-4/tp/pulls?q=is%3Apr+reviewed-by%3Awakululuu)
  * Reported [bugs and suggestions](https://github.com/wakululuu/ped/issues) for other teams in the class
