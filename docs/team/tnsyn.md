---
layout: page
title: Tan Su Yin's Project Portfolio Page
---

## Project: McScheduler

McScheduler is a **one-stop solution for McDonald's Shift Managers** to manage shift scheduling, as well as worker
contacts and compensation, optimized for use via a **Command Line Interface** (CLI) while still having the benefits of a
**Graphical User Interface** (GUI).

Given below are my contributions to the project.

* **New Feature**: Added the ability to edit existing assignments, where an assignment involves a worker, a shift and a role that the worker undertakes in the shift.  

  * What it does: 
    * Allows the user to edit existing assignments.
        * An existing assignment can be completely edited such that its worker, role and shift are changed.
        * Additionally, this enhancement can be used to reassign workers to a different role under the same shift, a different role under a different shift, or the same role under a different shift.
  * Justification: 
    * This feature improves the product significantly because a user can make mistakes when assigning workers to roles in shifts, or they might need to make adjustments to the assignments they previously made. The app should provide a convenient way to for users to be able to do this,
    rather than having users manually delete an existing assignment and then adding a new one.
  * Highlights:
    * Workers must be both available for the shift and suited for the role in order for the reassignment to be successful. Additionally, the enhancement ensures that no duplicate assignments are created as a result of the reassignment.
    * There are 2 possible command formats for this enhancement to improve usability. This is because in cases where users may want to edit the assignment drastically, more information is required of them. However, a simple reassignment within 
    a single shift for a single worker needs much lesser information. A shorter command format has been provided for the latter.
  * Credits: 
    * My groupmates who laid the foundation for assigning/unassigning workers and shift functionality.
  
  
* **New Feature**: Added the ability to specify a worker's unavailable timings.

  * What it does: 
    * Allows the user to add unavailable timings to a worker, which can be done at the start when users add a new worker to the McScheduler, or when users edit a worker's details.
        * Subsequently, workers cannot be assigned to shifts that they are unavailable for.
  * Justification: 
    * This feature improves the product significantly because McDonald's workers include part-time workers who are not available for work the entire week. The app should provide a convenient way to for users to record the unavailable timings of these workers,
    such that they do not wrongly assign these workers to shifts they are unavailable for.
  * Highlights:
    * Users are able to specify a day and a time for each unavailability. However, for cases where workers are unavailable for the entire day, only the day needs to be specified. 

* **Code contributed**: [RepoSense link](https://nus-cs2103-ay2021s1.github.io/tp-dashboard/#breakdown=true&search=tnsyn)

* **Enhancements to existing features**:
  * Edited the Tag field in Person (later refactored to Worker) to Role (Pull request [\#46]())
  * Refactored commands to include a 'worker-' prefix and parsers to include a 'worker' prefix (Pull requests [\#56](), [\#120]())

* **Documentation**:
  * User Guide:
    * Added documentation for the feature `reassign` [\#116]()
    * Edited documentation for `worker-add` and `worker-edit` to include unavailability fields [\#116]()
  * Developer Guide:
    * Added implementation details of the `unavailability` feature [\#94]()
    * Added implementation details of the `worker-add` feature [\#96]()
 
* **Community**:
  * PRs reviewed (with non-trivial review comments): [\#102](), [\#110](), [\#57](), [\#40]()
  * Reported [bugs and suggestions](https://github.com/tnsyn/ped) for other teams in the cohort
