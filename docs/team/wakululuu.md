---
layout: page
title: Foo Kai En | Project Portfolio Page
---

## Project: McScheduler

Developed during a Software Engineering introductory module, the McScheduler is a desktop application, targeted at
McDonald's managers for easy shift and worker management. The McScheduler is optimized for use via a CLI, while still
having benefits of a GUI, created with JavaFX. This project is written in Java in OOP style, and has about 10 kLoC.

Given below are my contributions to the project.

* **New feature**: Added the ability to maintain a master list of valid roles in the system
  * _What it does:_
    * Allows the user to add, edit and delete roles which can be taken on by workers in a McDonald's outlet
    * Ensures that any role added to the list of roles that a worker is able to take on is a valid role in the system
    * Ensures that any role added to the list of roles that need to be filled in a shift is a valid role in the system 
  * _Justification:_
    * Provides a convenient way for the user to rename or delete all instances of a role with a single command
    * Helps the user ensure the consistent naming of a role throughout the system
    * Alerts the user when there is a typo
  * _Highlights:_
    * The implementation was challenging as it required adding checks to many existing commands to ensure that all roles
      in the app, which include the list of roles that a worker is able to take on and those that need to be filled in a
      shift, are valid in the master list of roles


* **New feature**: Added the ability to assign/unassign a worker to/from a shift
  * _What it does:_
    * Allows the user to assign a worker to a role in a shift, as well as delete an existing assignment
    * Ensures that the role in the assignment is a role the worker is fit for
    * Ensures that the role in the assignment is required and has yet to be filled in the shift

<div style="page-break-after: always;"></div>

  * _Justification:_
    * Fulfils the fundamental purpose of the McScheduler, which is to allow the user to schedule workers for shifts
    * Helps the user ensure an assignment is valid (i.e. the worker is fit for the role and the shift has not been filled)
  * _Highlights:_
    * The feature required an in-depth analysis of design alternatives to avoid unintended effects of a cyclic
      dependency between the `Worker` and `Shift` classes
  * _Notes:_
    * This feature has since been enhanced by my teammates to:
      * Check if the worker is available and not on leave for the shift
      * Allow the mass assigning/unassigning of multiple workers to/from a shift with a single command


* **Enhancements to existing features**:
  * Revamped the GUI (PR [\#193](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/193))
  * Added tests for the new assign/unassign feature, increasing statement coverage from 0% to more than 90% for the
    related command and command parser classes (PR [\#97](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/97))


* **Code contributed**: [RepoSense](https://nus-cs2103-ay2021s1.github.io/tp-dashboard/#breakdown=true&search=wakululuu)


* **Documentation**:
  * User Guide:
    * Added documentation for the [`assign`/`unassign` feature](../UserGuide.md#assigning-a-worker-to-a-role-in-a-shift-assign)
    * Added documentation for the [`role-add`/`role-edit`/`role-delete` feature](../UserGuide.md#adding-a-role-role-add)
    * Updated notes about the command format and did cosmetic tweaks in general (PR [\#114](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/114))
  * Developer Guide:
    * Added implementation details and UML diagrams for the [`assign`/`unassign` feature](../DeveloperGuide.md#assignunassign-feature)
    * Updated user stories


* **Project management**:
  * Updated the Java CI and codecov badges on the product website to reflect our team's progress


* **Community**:
  * PRs reviewed (with non-trivial review comments): [\#107](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/107)
    [\#112](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/112) [\#113](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/113)
    [\#189](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/189)
  * Reported [bugs and suggestions](https://github.com/wakululuu/ped/issues) for other teams in the class
  * Contributed to the [class forum](https://github.com/nus-cs2103-AY2021S1/forum/issues/11) 
