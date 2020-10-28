---
layout: page
title: User Guide
---

McScheduler is a **one-stop solution for McDonald's Shift Managers** to manage shift scheduling, as well as worker
contacts and compensation, optimized for use via a **Command Line Interface** (CLI) while still having the benefits of a
**Graphical User Interface** (GUI). With the McScheduler, all information needed for shift-work assignment is made
easily available to streamline the work of McDonald's Shift Managers.


* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `11` or above installed on your computer.

1. Download the latest `mcscheduler.jar` [here](https://github.com/AY2021S1-CS2103-F10-4/tp/releases).

1. Copy the file to the folder you wish to use as the _home folder_ for the McScheduler.

1. Double-click the file to start the app. The GUI (similar to the image below) should appear in a few seconds. The app
   contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type a command in the command box and press `Enter` to execute it.<br>
   e.g. Typing **`help`** and pressing `Enter` will open the help window.<br>
   Some example commands you can try:

   * **`worker-list`** : Lists all workers in the McScheduler.

   * **`worker-add`**` n/John hp/98765432 a/21 Lower Kent Ridge Rd, Singapore 119077 r/Cashier p/7` : Adds a cashier
   named John whose pay is $7/hr. His phone number is 98765432 and he lives at 21 Lower Kent Ridge Rd, Singapore 119077.

   * **`worker-delete`**` 3` : Deletes the 3rd worker shown in the worker list.

   * **`shift-add`**` d/Wed t/AM r/Cashier 2 r/Janitor 3` : Adds a Wednesday AM shift, which requires 2 workers to fill
   the cashier role and 3 workers to fill the janitor role.

   * **`assign`**` s/3 w/2 r/Cashier` : Assigns the 2nd worker on the list to the 3rd shift on the list as a cashier.

   * **`exit`** : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in the command `worker-delete WORKER_INDEX`, `WORKER_INDEX` is a parameter for the index of the worker (in the
  worker list) you wish to delete from the application. The command can be used as `worker-delete 4`.

* Parameters in `[square brackets]` are optional.<br>
  e.g. `n/NAME [r/ROLE]` can be used as `n/John Doe r/Cashier` or as `n/John Doe`.

* Parameters with `…`​ after them can be used multiple times.<br>
  e.g. `w/WORKER_INDEX` can be used as `w/1`, `w/1 w/2`, `w/1 w/2 w/3` etc.

* When `[square brackets]` are used with `…`​, parameters can be used multiple times including zero times.<br>
  e.g. `[r/ROLE NUMBER_NEEDED]…​` can be used as ` ` (i.e. 0 times), `r/Cashier 3`, `r/Cashier 1 r/Janitor 2` etc.

* Parameters in `{curly brackets}` are a set and should be used together.
  e.g. `{w/WORKER_INDEX ROLE}…​` should be used as `w/1 Cashier`, `w/1 Cashier w/2 Janitor w/3 Chef` etc.

* Parameters can be provided in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

</div>

### Viewing help: `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`

### Adding a worker: `worker-add`

Adds a new worker to the McScheduler.

Format: `worker-add n/NAME hp/PHONE_NUMBER a/ADDRESS [p/HOURLY_PAY] [r/ROLE]...`

* Adds a worker with the specified `NAME`, `PHONE_NUMBER`, `ADDRESS` and `HOURLY_PAY`.
* The worker will be fit to take on the specified `ROLE`(s) in a shift. The specified `ROLE`(s) must be an existing role
  in the McScheduler. A role can be added to the McScheduler using the [role-add](#adding-a-role-role-add) command.

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**

A worker can be fit for any number of roles (including zero) that can be added by multiple `r/` parameters.

</div>

Examples:
* `worker-add n/John hp/98765432 a/21 Lower Kent Ridge Rd, Singapore 119077 r/Cashier p/7` Adds a cashier named John
  whose pay is $7/hr. His phone number is 98765432 and he lives at 21 Lower Kent Ridge Rd, Singapore 119077.

* `worker-add n/Tom hp/87654321 a/22 Bong Keng Road, #01–01 r/Burger Flipper r/Janitor p/7.50` Adds a worker named Tom
  who is fit to be a burger flipper or a janitor, and whose pay is $7.50/hr. His phone number is 87654321 and he lives
  at Bong Keng Road, #01–01.

### Listing all workers: `worker-list`

Shows a list of all workers in the McScheduler, including their contacts, hourly pay, assigned shifts and unavailable
timings.

Format: `worker-list`

### Editing a worker: `worker-edit`

Edits an existing worker in the McScheduler.

Format: `worker-edit WORKER_INDEX [n/NAME] [hp/PHONE_NUMBER] [a/ADDRESS] [p/HOURLY_PAY] [r/ROLE]...`

* Edits the worker at the specified `WORKER_INDEX`. The worker index refers to the index number shown in the displayed
  worker list. The worker index **must be a positive integer** i.e. 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing roles, the existing roles of the worker will be removed i.e adding of roles is not cumulative.
* The specified `ROLE`(s) must be an existing role in the McScheduler. A role can be added to the McScheduler using the
  [role-add](#adding-a-role-role-add) command.

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**

You can remove all of a worker’s roles by typing `r/` without specifying any roles after it.

</div>

Examples:
* `worker-edit 1 n/John r/Janitor` Edits the name and role of the 1st worker to be John and janitor respectively.

* `worker-edit 2 n/Betsy Crower p/7` Edits the name and pay of the 2nd worker to be Betsy Crower and $7/hr respectively.

<!-- ### Locating workers by name: `worker find`

Finds workers whose names contain any of the given keywords.

Format: `worker find KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g. `hans` will match `Hans`
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

* Deletes the worker at the specified `WORKER_INDEX`. The index refers to the index number shown in the displayed worker
  list. The worker index **must be a positive integer** i.e. 1, 2, 3, …​

Example:
* `worker-delete 4` Deletes the 4th worker shown in the worker list.

### Adding a shift: `shift-add`

Adds a new shift to the McScheduler.

Format: `shift-add d/DAY t/TIME [r/ROLE NUMBER_NEEDED]...`

* Adds a shift on the specified `DAY` at the specified `TIME`.
* The day specified should take one of these values: **Mon, Tue, Wed, Thu, Fri, Sat, Sun**. These values are case-
  insensitive i.e. `Mon`, `MON`, `mon`, `mOn` etc. are all accepted.
* The time specified should take one of these values: **AM, PM**. These values are case-insensitive i.e. `am`, `AM`,
  `aM` and `Am` are all accepted.
* The shift will require the specified `ROLE`(s). The specified `ROLE`(s) must be an existing role in the McScheduler. A
  role can be added to the McScheduler using the [role-add](#adding-a-role-role-add) command.
* Each role should be accompanied by the `NUMBER_NEEDED` to fill that role. This number **must be a positive integer**
  i.e. 1, 2, 3, …​

Examples:
* `shift-add d/Wed t/AM r/Cashier 2 r/Janitor 3` Adds a Wednesday AM shift, which requires 2 workers to fill the cashier
  role and 3 workers to fill the janitor role.

* `shift-add d/MOM t/pM` Adds a Monday PM shift with no required role yet.

### Listing all shifts: `shift-list`

Shows a list of all shifts in the McScheduler, including the roles needed and workers assigned to each shift.

Format: `shift-list`

### Editing a shift: `shift-edit`

Edits the details of an existing shift in the McScheduler.

Format: `shift-edit SHIFT_INDEX [d/DAY] [t/TIME] [r/ROLE NUMBER_NEEDED]...`

* Edits the shift at the specified `SHIFT_INDEX`. The shift index refers to the index number shown in the displayed
  shift list. The shift index **must be a positive integer** i.e. 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* The day specified should take one of these values: **Mon, Tue, Wed, Thu, Fri, Sat, Sun**. These values are case-
  insensitive i.e. `Mon`, `MON`, `mon`, `mOn` etc. are all accepted.
* The time specified should take one of these values: **AM, PM**. These values are case-insensitive i.e. `am`, `AM`,
  `aM` and `Am` are all accepted.
* The specified `ROLE`(s) must be an existing role in the McScheduler. A role can be added to the McScheduler using the
  [role-add](#adding-a-role-role-add) command.
* Each role should be accompanied by the `NUMBER_NEEDED` to fill that role. This number **must be a positive integer**
  i.e. 1, 2, 3, …​

Examples:
* `shift-edit 3 r/Cashier 3 r/Janitor 2` Edits the 3rd shift on the list such that it now requires 3 cashiers and 2
  janitors.

* `shift-edit 1 d/Mon t/PM r/Janitor 1` Edits the 1st shift such that it is now a Monday PM shift, requiring 1 janitor.

* `shift-edit 2 r/` Edits the 2nd shift such that it now has no required roles.

### Deleting a shift: `shift-delete`

Deletes a shift from the McScheduler.

Format: `shift-delete SHIFT_INDEX`

* Deletes the shift at the specified `SHIFT_INDEX`. The index refers to the index number shown in the displayed shift
  list. The shift index **must be a positive integer** i.e. 1, 2, 3, …​

Example:
* `shift-delete 2` Deletes the 2nd shift on the list.

### Adding a role: `role-add`

Adds a new role to the McScheduler.

Format: `role-add ROLE`

* Adds the specified `ROLE` to the McScheduler. The specified `ROLE` should be alphanumeric and can contain whitespaces.

Examples:
* `role-add cashier` Adds a cashier role.

* `role-add Storey 2 server` Adds a storey 2 server role.

### Listing all roles: `role-list`

Shows a list of all roles in the McScheduler.

Format: `role-list`

### Deleting a role: `role-delete`

Deletes a role from the McScheduler.

Format: `role-delete ROLE_INDEX`

* Deletes the role at the specified `ROLE_INDEX`. The index refers to the index number shown in the role list displayed
  using the [role-list](#listing-all-roles-role-list) command. The role index **must be a positive integer** i.e. 1, 2,
  3, …​

Example:
* `role-delete 3` Deletes the 3rd role shown in the role list.

### Assigning a worker to a role in a shift: `assign`

Assigns an existing worker to take on an existing role in an existing shift.

Format: `assign s/SHIFT_INDEX w/WORKER_INDEX r/ROLE`

* Assigns the worker at the specified `WORKER_INDEX` to the shift at the specified `SHIFT_INDEX`. The indexes refer to
  the index numbers shown in the displayed worker and shift lists. The indexes **must be positive integers** i.e. 1, 2,
  3, …​
* The assigned worker will fill up the specified `ROLE` in the shift. The worker must be fit for the specified `ROLE`
  and the shift must require the `ROLE`.

Example:
* `assign s/3 w/2 r/Cashier` Assigns the 2nd worker on the worker list to the 3rd shift on the shift list as a cashier.

### Removing a worker from a shift: `unassign`

Removes a worker from a particular shift.

Format: `unassign s/SHIFT_INDEX w/WORKER_INDEX`

* Unassigns the worker at the specified `WORKER_INDEX` from the shift at the specified `SHIFT_ INDEX`. The indexes refer
  to the index numbers shown in the displayed worker and shift lists. The indexes **must be positive integers** i.e. 1,
  2, 3, …​

Example:
* `unassign s/4 w/1` Removes the 1st worker from the 4th shift.

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

McScheduler data are saved in the hard disk automatically after any command that changes the data. There is no need to
save manually.

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains
the data of your previous McScheduler home folder.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Data | Action | Format, Example
-----|--------|------------------
Worker | **Add** | `worker-add n/NAME hp/PHONE_NUMBER a/ADDRESS [p/HOURLY_PAY] [r/ROLE]...`<br>e.g. `worker-add n/Johnhp/98765432 a/21 Lower Kent Ridge Rd, Singapore 119077 r/Cashier p/7`
Worker | **Delete** | `worker-delete WORKER_INDEX`<br>e.g. `worker-delete 4`
Worker | **Edit** | `worker-edit WORKER_INDEX [n/NAME] [hp/PHONE_NUMBER] [a/ADDRESS] [p/HOURLY_PAY] [r/ROLE]...`<br>e.g. `worker-edit 2 n/Betsy Crower p/7`
Worker | **List** | `worker-list`
Shift | **Add** | `shift-add d/DAY t/TIME [r/ROLE NUMBER_NEEDED]...`<br>e.g. `shift-add d/Wed t/AM r/Cashier 2 r/Janitor 3`
Shift | **Delete** | `shift-delete SHIFT_INDEX`<br>e.g. `shift-delete 2`
Shift | **Edit** | `shift-edit SHIFT_INDEX [d/DAY] [t/TIME] [r/ROLE NUMBER_NEEDED]...`<br>e.g. `shift-edit 1 d/Mon t/PM r/Janitor 1`
Shift | **List** | `shift-list`
Role | **Add** | `role-add ROLE`<br>e.g. `role-add Storey 2 server`
Role | **Delete** | `role-delete ROLE_INDEX`<br>e.g. `role-delete 3`
Role | **List** | `role-list`
Assignment | **Assign** | `assign s/SHIFT_INDEX w/WORKER_INDEX r/ROLE`<br>e.g. `assign s/3 w/2 r/Cashier`
Assignment | **Unassign** | `unassign s/SHIFT_INDEX w/WORKER_INDEX`<br>e.g. `unassign s/4 w/1`
General | **Help** | `help`
General | **Exit** | `exit`
