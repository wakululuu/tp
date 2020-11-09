---
layout: page
title: Sigmund Chianasta's Project Portfolio Page
---

## Project: McScheduler

McScheduler is a **one-stop solution for McDonald's Shift Managers to manage shift scheduling and worker
contact/compensation, optimized for use via a Command Line Interface** (CLI) while still having the benefits of a
Graphical User Interface (GUI).

Given below are my contributions to the project.

* **New Feature**: Added mass operations for Assign and Unassign
  * What it does: allows the user to assign multiple workers to a shift in a single call.
  * Justification: allow managers to assign workers quickly, especially to fill in newly added shifts.
  * Highlights: mass-ops uses already existing features so that it doesn't add further complexity for users who are not using mass-ops. They can still add assignments one by one.
  As extension of Assign and Unassign, Take-leave and Cancel-leave is also able to make use of the mass-ops function.
    * The WorkerRolePair class was created to help make mass-ops more manageable.
  * Credits: My amazing teammates for laying the groundwork with assigment and other commands to make implementation of mass-ops easier.
  
  **New Feature**: Added McDonald's jingle when opening the app
  * What it does: plays the McDonald's jingle when opening the app.
  * Justification: its a neat little feature that we think users would appreciate by reminding them that they are a part of the McFamily.
  * Highlights: added JavaFX's media dependency to play the jingle.
  * Credits: McDonald's for creating the jingle, [this youtube video](https://www.youtube.com/watch?v=SE1B3N_a7fE) for uploading it.
  
* **Code contributed**: [RepoSense link](https://nus-cs2103-ay2021s1.github.io/tp-dashboard/#breakdown=true&search=sigmund-c)
  * NOTE: I refactored ab3 -> mcsheduler in PR [#121](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/121), which then authored a lot of changes to me. Though, my teammates have added the @author tags to assign their important works as theirs. Please take note when reviewing my contributions!

* **Project management**:
  * Managed releases `v1.2` - `v1.3` (3 releases) on GitHub

* **Enhancements to existing features**:
  * Updated the initial GUI color scheme (Pull request [\#98](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/98))
  * Edited the Person model to Worker, with some minor parameter changes (Pull requests [\#40](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/40), [\#48](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/48), [\#57](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/57))
  * Added more specific error messages, indicating the user's wrong input (Pull request [\#194](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/194))
  * Changed app icon, app name, jar name, window name, and in general removing the existance of ab3 [\#98](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/98)

* **Documentation**:
  * User Guide:
    * Edited documentation for the mass-ops feature on assign/unassign [\#101](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/101)
    * Refactored person -> worker [\#57](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/57)
  * Developer Guide:
    * Added implementation details of the `MassOps` feature [#101](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/101)
      * Diagram contributed: 
![Object Diagram of MassAssign](images/MassAssignObjectDiagram.png)
    * Fixed diagram inconsistencies [\#234](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/234)
  * Miscellaneous:
    * Created a mock-up UI to base work around (updated by a teammate) [\#26](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/26)

* **Community**:
  * PRs reviewed (with non-trivial review comments): [\#30](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/30), [\#117](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/117)
  * Helped schedule team meetings before we have a strict schedule, directed the initial work with project notes and other internal documents

