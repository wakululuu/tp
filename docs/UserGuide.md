---
layout: page
title: User Guide
---

McScheduler is a **one-stop solution for McDonald's Shift Managers to manage shift scheduling and worker contact/compensation, optimized for use via a Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). With the McScheduler, all information needed for shift-work assignment is made available easily to streamline the work of McDonald's Shift Managers.


* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `11` or above installed in your Computer.

1. Download the latest `mcscheduler.jar` from [here](https://github.com/AY2021S1-CS2103-F10-4/tp/releases).

1. Copy the file to the folder you want to use as the _home folder_ for McScheduler.

1. Double-click the file to start the app. The GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * **`worker-list`** : Lists all workers in the McScheduler.

   * **`worker-add`**`n/John hp/98765432 a/21 Lower Kent Ridge Rd, Singapore 119077 r/Cashier p/7` : Adds a cashier named John whose pay is $7/hr. His phone number is 98765432 and he lives at 21 Lower Kent Ridge Rd, Singapore 119077.

   * **`worker-delete`**`3` : Deletes the 3rd worker shown in the worker list.

   * **`shift-add`**`d/Wed t/AM r/Cashier-2 r/Cleaner-3` : Adds a shift on Wednesday morning, which requires 2 workers to fill the cashier role and 3 workers to fill the cleaner role.

   * **`assign`**`s/3 w/2 r/Cashier` : Assign the 2nd worker on the list to the 3rd shift on the list as a Cashier.

   * **`exit`** : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `worker-delete WORKER_INDEX`, `WORKER_INDEX` is a parameter for the index of the worker (in the worker list) you want to delete from the application which can be used as `worker-delete 4`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [r/ROLE]` can be used as `n/John Doe r/Cashier` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[r/ROLE NUMBER_NEEDED]…​` can be used as ` ` (i.e. 0 times), `r/Cashier 3`, `r/Cashier 1 r/Cleaner 2` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

</div>

### Viewing help: `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`

### Adding a worker: `worker-add`

Adds a new worker into the McScheduler.

Format: `worker-add n/NAME hp/PHONE_NUMBER a/ADDRESS [p/HOURLY_PAY] [r/ROLE]...`

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
A worker can have any number of roles (including 0) that are added by multiple r/ arguments.
</div>

* The pay is given as hourly pay rate.

Examples:
* `worker-add n/John hp/98765432 a/21 Lower Kent Ridge Rd, Singapore 119077 r/Cashier p/7` Adds a cashier named John whose pay is $7/hr. His phone number is 98765432 and he lives at 21 Lower Kent Ridge Rd, Singapore 119077.

* `worker-add n/Tom hp/87654321 a/22 Bong Keng Road, #01–01 r/Burger Flipper r/Cleaner p/7.50` Adds a worker named Tom who is able to be a Burger Flipper or a Cleaner, whose pay is $7.50/hr. His phone number is 87654321 and he lives at Bong Keng Road, #01–01.

### Listing all workers: `worker-list`

Shows a list of all workers in the McScheduler.

Format: `worker-list`

### Editing a worker: `worker-edit`

Edits an existing worker in the McScheduler.

Format: `worker-edit WORKER_INDEX [hp/PHONE_NUMBER] [a/ADDRESS] [n/NAME] [p/HOURLY_PAY] [r/ROLE]...`

* Edits the worker at the specified `WORKER_INDEX`. The worker index refers to the index number shown in the displayed worker list. The worker index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing roles, the existing roles of the worker will be removed i.e adding of roles is not cumulative.
* You can remove all the worker’s roles by typing `r/` without specifying any roles after it.

Examples:
*  `worker-edit 1 n/John r/Cleaner` Edits the name and worker role of the 1st worker to be John and Cleaner respectively.
*  `worker-edit 2 n/Betsy Crower p/7` Edits the name and pay of the 2nd worker to be Betsy Crower and $7/hr respectively.

<!-- ### Locating workers by name: `worker find`

Finds workers whose names contain any of the given keywords.

Format: `worker find KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Only the name is searched.
* Only full words will be matched e.g. `Han` will not match `Hans`
* Workers matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

Examples:
* `worker find John` returns `john` and `John Doe`
* `worker find alex david` returns `Alex Yeoh`, `David Li`<br>
  ![result for 'worker find alex david'](images/findAlexDavidResult.png) -->

### Deleting a worker: `worker-delete`

Deletes a worker from the McScheduler.

Format: `worker-delete WORKER_INDEX`

* Deletes the worker at the specified `WORKER_INDEX`. The index refers to the index number shown in the displayed worker list. The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `worker-delete 4` Deletes the 4th worker shown in the worker list.

### Adding a shift: `shift-add`

Adds a new shift to the McScheduler.

Format: `shift-add d/DAY t/TIME [r/ROLE NUMBER_NEEDED]...`

* Adds a shift on the specified ​day at the specified time.
* The day specified should be 1 of these values: **Mon, Tue, Wed, Thur, Fri, Sat, Sun**.
* The time specified should be 1 of these values: **AM/PM**.
* Each role should be accompanied by the number needed, and this number **must be a positive integer** 1, 2, 3...

Examples:
* `shift-add d/Wed t/AM r/Cashier 2 r/Cleaner 3` Adds a shift on Wednesday morning, which requires 2 workers to fill the cashier role and 3 workers to fill the cleaner role.

* `shift-add d/Mon t/PM` Adds a shift on Monday afternoon with no specified roles yet.

### Listing all shifts: `shift-list`

Shows a list of all shifts (including all details about the shifts such as their day, time and list of workers) in the McScheduler.

Format: `shift-list`

### Editing a shift: `shift-edit`

Edits the details of an existing shift in the McScheduler.

Format: `shift-edit SHIFT_INDEX [d/DAY] [t/TIME] [r/ROLE NUMBER_NEEDED]...`

* Edits the shift at the specified `SHIFT_INDEX`. The shift index refers to the index number shown in the displayed shift list. The shift index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* The day specified should be 1 of these values: **Mon, Tue, Wed, Thur, Fri, Sat, Sun**
* The time specified should be 1 of these values: **AM/PM**
* Each role should be accompanied by the number needed, and this number **must be a positive integer** 1, 2, 3...

Examples:
* `shift-edit 3 r/Cashier 3 r/Cleaner 2` Edits the 3rd shift on the list such that it now has 3 cashier roles and 2 cleaner roles.
* `shift-edit 1 d/Mon t/PM r/Cleaner 1` Edits the 1st shift such that it is now on Monday afternoon, with 1 cleaner role
* `shift-edit 2 r/Cleaner-0` Edits the 2nd shift such that it now has no cleaner roles

### Deleting a shift: `shift-delete`

Deletes a shift from the McScheduler.

Format: `shift-delete SHIFT_INDEX`

* Deletes the shift at the specified `SHIFT_INDEX`. The index refers to the index number shown in the displayed shift list. The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `shift-delete 4` Deletes the 4th shift on the list.

### Assign worker to shift: `assign`

Assigns a worker to a particular shift.

Format: `assign s/SHIFT_INDEX w/WORKER_INDEX r/ROLE`

* Assigns a worker to shift the specified SHIFT_INDEX. The worker will be at the specified WORKER_INDEX in the worker list. Both indexes must be a positive integer.
* The order of specifying does not matter, as long ‘s/’ is attached to the SHIFT_INDEX and ‘w/’ is attached to the WORKER_INDEX. <br> e.g. `assign s/4 w/1` is equivalent to `assign w/1 s/4 `
* The assigned worker will fill up the given ROLE in the shift.
* If the worker is already in the shift, they will be reassigned to the given role if possible.
  * This is equivalent to unassign followed by assign.
* An error message will be displayed in the following situations:
  * The shift does not have the role.
  * The role is already completely filled up in the shift.
  * The worker is unable to fulfil the role.


Examples:
* `assign s/3 w/2 r/Cashier` Assign the 2nd worker on the list to the 3rd shift on the list as a Cashier.

### Remove worker from shift: `unassign`

Removes a worker from a particular shift.

Format: `unassign s/SHIFT_INDEX w/WORKER_INDEX`

* Unassigns a worker to the shift at the specified `SHIFT_INDEX` of the shift list. The worker unassigned will be the worker at the specified `WORKER_INDEX` in the worker list.
* The order of specifying does not matter, as long 's/' is attached to the `SHIFT_INDEX` and 'w/' is attached to the `WORKER_INDEX`. <br> e.g. `unassign s/4 w/1` is equivalent to `unassign w/1 s/4`

Examples:
* `unassign s/4 w/1` Removes the 1st worker from the 4th shift.

### Assign worker to take leave during shift: `take-leave`

Assigns a worker to take leave at a particular day and time, as indicated by a shift.

Format: `take-leave s/SHIFT_INDEX w/WORKER_INDEX`

* Assigns a worker to take leave on the shift at the specified `SHIFT_INDEX` in the shift list. The worker taking leave
will be the worker at the specified `WORKER_INDEX` in the worker list.
* The order of specifying does not matter, as long as 's/' is attached to the `SHIFT_INDEX` and 'w/' is attached to the 
`WORKER_INDEX`. <br> e.g. `take-leave s/4 w/1` is equivalent to `take-leave w/1 s/4`.
* An error message will be shown in the following situations:
  * The worker is unavailable for that shift, since there is no reason to take leave then.
  * The worker is already assigned to a role for that shift.

Examples:
* `take-leave s/4 w/1` Assigns the 1st worker to take leave during the 4th shift.

### Assign a worker's leave over a range of days and times: `mass-take-leave`

Assigns a worker to take leave over a range of days and times given a start and end day/time.

Format: `mass-take-leave w/WORKER_INDEX d/START_DAY t/START_TIME d/END_DAY t/END_TIME`

* Assigns a worker to take leave on all shifts between the specified `START_DAY` and `START_TIME` to `END_DAY` and 
`END_TIME`. The worker taking leave will be the worker at the specified `WORKER_INDEX` in the worker list.
* The order of specifying **does matter** between the two sets of days and times (i.e. `START_DAY` must come before `END_DAY`
and similarly for time). Specifying in the wrong order is likely to result in leave taken in the wrong shifts.
* However, the order does not matter for all other arguments. <br> e.g. `mass-take-leave w/2 d/MON t/AM d/FRI t/PM` is
the same as `mass-take-leave t/AM t/PM d/MON d/FRI w/2`, though the latter syntax is not recommended.
* The day/time range will loop properly between Sunday and Monday. Hence `mass-take-leave w/2 d/SUN t/AM d/MON t/AM` will
work as intended - leave taken on Sunday morning to Monday morning.
* Shifts will be created for all day and time combinations within the specified range that does not have a shift already
present within the McScheduler. These shifts will have no role requirements.
* An error message will be shown in the following situations:
  * The worker has an assigned role in any one of the shifts within the day/time range.
  
Examples:
* `mass-take-leave w/2 d/MON t/PM d/THU t/PM` Assigns the 2nd worker to take leave from MON PM shift to THU PM shift (inclusive).
* `mass-take-leave w/1 d/THU t/PM d/MON t/PM` Assigns the 1st worker to take leave from THU PM shift to MON PM shift (inclusive).

### Cancel a worker's leave during a shift: `cancel-leave`

Cancels a worker's leave at a particular day and time, as indicated by a shift.

Format `cancel-leave s/SHIFT_INDEX w/WORKER_INDEX`

* Cancel's a worker's leave on the shift at the specified `SHIFT_INDEX` in the shift list. The worker whose leave is 
cancelled will be the worker at the specified `WORKER_INDEX` in the worker list.
* The order of specifying does not matter, as long as 's/' is attached to the `SHIFT_INDEX` and 'w/' is attached to the
`WORKER_INDEX`. <br> e.g. `cancel-leave s/4 w/1` is equivalent to `cancel-leave w/1 s/4`.
* An error message will be shown in the following situations:
  * No leave found for the worker at the specified shift.
  * An assignment other than leave is found for the worker at the specified shift.
  
Examples:
* `cancel-leave s/4 w/1` Cancels the leave of the 1st worker for the 4th shift.

### Cancel a worker's leave over a range of days and times: `mass-cancel-leave`

Cancels a worker's leave over a range of days and times given a start and end day/time.

Format: `mass-cancel-leave w/WORKER_INDEX d/START_DAY t/START_TIME d/END_DAY t/END_TIME`

* Cancel's a worker's leave on all shifts between the specified `START_DAY` and `START_TIME` to `END_DAY` and 
`END_TIME`. The worker cancelling leave will be the worker at the specified `WORKER_INDEX` in the worker list.
* The order of specifying **does matter** between the two sets of days and times (i.e. `START_DAY` must come before `END_DAY`
and similarly for time). Specifying in the wrong order is likely to result in leave cancelled in the wrong shifts.
* However, the order does not matter for all other arguments. <br> e.g. `mass-cancel-leave w/2 d/MON t/AM d/FRI t/PM` is
the same as `mass-cancel-leave t/AM t/PM d/MON d/FRI w/2`, though the latter syntax is not recommended.
* The day/time range will loop properly between Sunday and Monday. Hence `mass-cancel-leave w/2 d/SUN t/AM d/MON t/AM` will
work as intended - leave cancelled from Sunday morning to Monday morning.
* An error message will be shown in the following situations:
  * The worker has no leave in the given day/time range.
  
Examples:
* `mass-cancel-leave w/2 d/MON t/PM d/THU t/PM` Cancels the 2nd worker's leave between MON PM shift to THU PM shift (inclusive).
* `mass-cancel-leave w/1 d/THU t/PM d/MON t/PM` Cancels the 1st worker leave between THU PM shift to MON PM shift (inclusive).

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

McScheduler data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

<!-- ### <Insert new feature> `[coming in v2.0]`

_{explain the feature here}_ -->

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous McScheduler home folder.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Data | Action | Format, Examples
-----|--------|------------------
Worker | **Add** | `worker-add n/NAME hp/PHONE_NUMBER a/ADDRESS [r/ROLE] [p/HOURLY_PAY]​` <br> e.g., `worker-add n/John hp/98765432 a/21 Lower Kent Ridge Rd, Singapore 119077 r/Cashier p/7`
Worker | **Delete** | `worker-delete WORKER_INDEX​` <br> e.g., `worker-delete 4`
Worker | **Edit** | `worker-edit WORKER_INDEX [n/NAME] [hp/PHONE_NUMBER] [a/ADDRESS] [r/ROLE] [p/HOURLY_PAY]​` <br> e.g., `worker-edit 2 n/Betsy Crower p/7`
Worker | **List** | `worker-list`
Shift | **Add** | `shift-add d/DAY t/TIME [r/ROLE-NUMBER_NEEDED]...​` <br> e.g., `shift-add d/Wed t/AM r/Cashier-2 r/Cleaner-3`
Shift | **Delete** | `shift-delete SHIFT_INDEX​` <br> e.g., `shift-delete 4`
Shift | **Edit** | `shift-edit SHIFT_INDEX [d/DAY] [t/TIME] [r/ROLE-NUMBER_NEEDED]...` <br> e.g., `shift-edit 1 d/Mon t/PM r/Cleaner-1`
Shift | **List** | `shift-list`
General | **Assign** | `assign s/SHIFT_INDEX w/WORKER_INDEX r/ROLE​` <br> e.g., `assign s/3 w/2 r/Cashier`
General | **Unassign** | `unassign s/SHIFT_INDEX w/WORKER_INDEX` <br> e.g., `unassign s/4 w/1`
General | **Help** | `help`
