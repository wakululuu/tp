---
layout: page
title: Guo Bohao's Project Portfolio Page
---

## Project: McScheduler

McScheduler is a **one-stop solution for McDonald's Shift Managers** to manage shift scheduling, as well as worker
contacts and compensation, optimized for use via a **Command Line Interface** (CLI) while still having the benefits of a
**Graphical User Interface** (GUI). With the McScheduler, all information needed for shift-work assignment is made
easily available to streamline the work of McDonald's Shift Managers.

The user interacts with it using a CLI, and it has a GUI created with JavaFX. It is written in Java, and has about 10 kLoC.

My contributions to the project are detailed below.

* **New Feature**: Added the ability check pay earned by a worker for the week. (Pull request [\#112](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/112))
  * What it does: Calculate the pay earned by a worker for the week based on the shifts assigned to the worker for the week.
  * Justification: This feature allows shift managers to know the pay earned by each worker and check against the worker's actual pay check.
  * Highlights: The command outputs the calculation used for the worker pay `Bernice Yu's pay for the week:`<br>
                                                                            `$11.20/hr x 8hr/shift x 2 shift(s) = $179.20`
  
* **New Feature**: Added the ability to view the list of available workers who can work for a given shift under a given role. (Pull request [\#117](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/117))
  * What it does: Shows a list of workers who are able to work for a given shift under a given role by checking if the worker is fit for the role, if the worker is on leave or if the worker is unavailable for the shift.
  * Justification: This feature allows shift managers to quickly check who are available to work for a given shift under a given role instead of manually checking each worker.
  
* **Code contributed**: [RepoSense link](https://nus-cs2103-ay2021s1.github.io/tp-dashboard/#breakdown=true&search=plosslaw)

* **Enhancements to existing features**:
  * Changed phone number to be strictly Singapore phone numbers only (8 digits, starting with 6, 8 or 9) (Pull request [\#204](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/204))
  * Added tests for `Shift` related commands, `worker-pay` and `worker-avail` (Pull requests [\#53](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/53), [\#112](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/112), [\#117](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/117))
  * Made commands case-insensitive (Pull request [\#42](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/42))
  * Made McScheduler throw more customized and readable errors to the user (Pull request [\#252](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/252))
  * Made each RoleRequirement in the Shift list start on a new line and use wrapText to reduce clutter (Pull request [\#232](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/232))
  * Prevented multiple roles from being added to a `Shift` (Pull request [\#204](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/204))
  * Added unexpected parameter check for commands that do not need input parameters (Pull request [\#204](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/204))
  * Changed `AssignCommand` to throw error when trying to assign duplicate workers (duplicate worker indexes being detected) (Pull request [\#204](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/204))

* **Documentation**:
  * User Guide:
    * Updated AB3 user guide to match McScheduler (Pull request [\#26](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/26))
    * Added documentation for `worker-pay` and `worker-avail` commands (Pull request [\#122](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/122))
  * Developer Guide:
    * Added implementation details of the `Role` feature. (Pull request [\#122](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/122))
    * Added Use Cases for `Role` and `Leave` related commands (Pull request [\#262](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/262))

* **Bug fixes and refactoring**:
  * Fix bugs found during dry PE (Pull requests [\#204](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/204), [\#232](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/232))
  * Removed Tag class (Pull request [\#129](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/129))
  * Limited role names to maximum of 50 chars to avoid long role names being hidden in the GUI (Pull request [\#232](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/232))
  * Fixed inconsistent invalid index error messages (Pull request [\#249](https://github.com/AY2021S1-CS2103-F10-4/tp/pull/249))

* **Community**:
  * Reported [bugs and suggestions](https://github.com/plosslaw/ped/issues) for Practical Exam (Dry Run)


