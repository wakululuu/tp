---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<img src="images/ArchitectureDiagram.png" width="450" />

The ***Architecture Diagram*** given above explains the high-level design of the App. Given below is a quick overview of each component.

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document can be found in the [diagrams](https://github.com/se-edu/addressbook-level3/tree/master/docs/diagrams/) folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.

</div>

**`Main`** has two classes called [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java). It is responsible for,
* At app launch: Initializes the components in the correct sequence, and connects them up with each other.
* At shut down: Shuts down the components and invokes cleanup methods where necessary.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

The rest of the App consists of four components.

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

Each of the four components,

* defines its *API* in an `interface` with the same name as the Component.
* exposes its functionality using a concrete `{Component Name}Manager` class (which implements the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component (see the class diagram given below) defines its API in the `Logic.java` interface and exposes its functionality using the `LogicManager.java` class which implements the `Logic` interface.

![Class Diagram of the Logic Component](images/LogicClassDiagram.png)

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

The sections below give more details of each component.

### UI component

![Structure of the UI Component](images/UiClassDiagram.png)

**API** :
[`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `WorkerListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class.

The `UI` component uses JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* Executes user commands using the `Logic` component.
* Listens for changes to `Model` data so that the UI can be updated with the modified data.

### Logic component

![Structure of the Logic Component](images/LogicClassDiagram.png)

**API** :
[`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

1. `Logic` uses the `McSchedulerParser` class to parse the user command.
1. This results in a `Command` object which is executed by the `LogicManager`.
1. The command execution can affect the `Model` (e.g. adding a worker).
1. The result of the command execution is encapsulated as a `CommandResult` object which is passed back to the `Ui`.
1. In addition, the `CommandResult` object can also instruct the `Ui` to perform certain actions, such as displaying help to the user.

Given below is the Sequence Diagram for interactions within the `Logic` component for the `execute("delete 1")` API call.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>

### Model component

![Structure of the Model Component](images/ModelClassDiagram.png)

**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

The `Model`,

* stores a `UserPref` object that represents the user’s preferences.
* stores the address book data.
* exposes an unmodifiable `ObservableList<Worker>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* does not depend on any of the other three components.


<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `McScheduler`, which `Worker` references. This allows `McScheduler` to only require one `Tag` object per unique `Tag`, instead of each `Worker` needing their own `Tag` object.<br>
![BetterModelClassDiagram](images/BetterModelClassDiagram.png)

</div>


### Storage component

![Structure of the Storage Component](images/StorageClassDiagram.png)

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

The `Storage` component,
* can save `UserPref` objects in json format and read it back.
* can save the address book data in json format and read it back.

### Common classes

Classes used by multiple components are in the `mcscheduler.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Adding of a Worker feature

The adding of workers is core to the functionality of the system. Users are able to add important information to each
worker, which will help them assign workers to shifts they are most suited for.

#### Implementation

The mechanism for adding a worker is facilitated by a `Worker` class. A `Worker` has a `Name`, a
`Phone`, a `Pay`, an optional `Role` set and an optional `Unavailability` set.

![Worker Class Diagram](images/WorkerClassDiagram.png)

A user can add a `Worker` to the `McScheduler` by running a `worker-add` command. 

#### Example usage scenario
Given below is an example usage scenario and how the add worker feature behaves at each step after the user has
launched the application.

Step 1. The user executes the command `worker-add n/John hp/98765432 p/9.0 a/400 Scheduler Lane`. `McSchedulerParser`
creates a `WorkerAddCommandParser` and calls the `WorkerAddCommandParser#parse()` method.

Step 2. The fields `n/`, `hp/`, `p/`, and `a/` are parsed within `WorkerAddCommandParser#parse()` and an instance of
`Name`, `Phone`, `Pay` and `Address` are created respectively. These objects are passed as parameters to the `Worker`
constructor and a new `Worker` object is created.

Step 3. A `WorkerAddCommand` with the newly created `Worker` object is returned and executed. The `Worker` object is
added to and stored inside the `Model`.

The following sequence diagram shows how `Worker` is added.

![Add Worker Sequence Diagram](images/AddWorkerSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `AddCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>

### Shift feature
Similar to workers, adding and manipulating shifts is a key functionality of the McScheduler. Managers will be able to make use of 
shifts to set role requirements, add or remove workers and assign leave. 

#### Implementation

Shifts are represented by a `Shift` class. It contains important detail related to shifts such as the day (through `ShiftDay`),
the time (through `ShiftTime`) and role requirements (through `RoleRequirement`) that details how many workers are needed
at which positions in a given shift.

The following diagram details `Shift` and how it is represented in the App model.

![Shift Class Diagram](images/ShiftClassDiagram.png)

#### Commands
The following commands have been implemented to work with `Shift`:
- `ShiftAddCommand` to add new shifts
- `ShiftEditCommand` to edit existing shifts
- `ShiftDeleteCommand` to delete existing shifts

These commands work similarly to the `Worker` based commands.

#### Example Usage Scenario
Given below is an example usage scenario and how the edit shift feature works at each step.

Step 1. User enters the command `shift-edit 2 d/FRI`. `McSchedulerParser` creates a `ShiftEditCommandParser` and calls
the `ShiftEditCommandParser#parser()` method.

Step 2. The preamble index and field `d/` are parsed within `ShiftEditCommandParser#parser()` and creates an instance of
`ShiftEditCommandParser` then creates a `ShiftEditDescriptor` with a new `ShiftDay`. Should there be other optional fields
such as `ShiftTime` or `RoleRequirement` as requested by the uder in their command, similar instances will be created and added
to the `ShiftEditDescriptor`.

Step 3. A `ShiftEditCommand` with the `ShiftEditDescriptor` and the index of the `Shift` of interest is returned and executed,
setting the edited shift within the model. This results in the replacement of the `Shift` object within the model with a newly
created `Shift` object based on the new attributes.

The following sequence diagram demonstrates this editing process (as per the example).

![Edit Shift Sequence Diagram](images/EditShiftSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** Should there be other information to be edited
as requested by the user, there will be other objects created besides `ShiftDay`.
</div>

### Unavailability feature

The unavailability feature allows users to add unavailable timings to a `Worker`, which comprise a day and/or a time.
The setting prevents workers from being assigned to shift slots that they are unavailable for.

#### Implementation

The proposed mechanism is facilitated by `ParserUtil` and the existing system for adding and editing workers.

#### Unavailability

Unavailability is represented by an `Unavailability` class. Since a worker's unavailable timings are only relevant
in the context of existing shift slots, `Unavailability` contains a `ShiftDay` and a `ShiftTime`.

![Unavailability Class Diagram](images/UnavailabilityClassDiagram.png)

Instances of `Unavailability` can be created on 2 occasions:

1. During a `worker-add` command, prefixed with `u/`
2. During a `worker-edit` command, prefixed with `u/`

To increase the efficiency of adding a worker's unavailable timings, users may type `u/UNAVAILABILITY_DAY` to indicate a full-day unavailability 
instead of typing `u/UNAVAILABILITY_DAY AM` and `u/UNAVAILABILITY_DAY PM` separately. Functionality has been added to support the creation
of an AM `Unavailability` and a PM `Unavailability` when `u/UNAVAILABILITY_DAY` is entered. The `ParserUtil` class supports this during parsing through:

- `ParserUtil#parseUnavailability()` — Parses a String and creates a valid `Unavailability` object
- `ParserUtil#createMorningUnavailabilityString()` — Generates a String of the format `UNAVAILABILITY_DAY AM`
- `ParserUtil#createAfternoonUnavailabilityString()` — Generates a String of the format `UNAVAILABILITY_DAY PM`
- `ParserUtil#parseUnavailabilities()` — Iterates through a collection of Strings and creates an `Unavailability`
object for each

#### Example usage scenario

Given below is an example usage scenario and how the unavailability feature behaves at each step after the user has
launched the application.

Step 1. The user executes a `worker-add` command `worker-add ... u/Mon`. `McSchedulerParser` creates a
`WorkerAddCommandParser` and calls the `WorkerAddCommandParser#parse()` method.

Step 2. Within `WorkerAddCommandParser#parse()`, `ParserUtil#parseUnavailabilities()` is called to generate an
`Unavailability` set from the given `u/Mon` field. `ParserUtil#parseUnavailabilities()` checks whether
the input contains only 1 keyword. In this case, since only 1 keyword (i.e. `Mon`) is present, `ParserUtil#createMorningUnavailabilityString()`
is called to generate a `Mon AM` String and `ParserUtil#createAfternoonUnavailabilityString()` is called to generate a `Mon PM`
String. Inside `ParserUtil#parseUnavailabilities()`, `ParserUtil#parseUnavailability()` is called on both Strings
and 2 valid `Unavailability` objects are created, before being added to the returnable `Unavailability` set.

Step 3. The `Unavailability` set is passed into the constructor of the `Worker` class to instantiate a `Worker` object
with the unavailable timings `Mon AM` and `Mon PM`.

The following sequence diagram shows how unavailable timings are added to a `Worker`.

![Unavailability Sequence Diagram](images/AddUnavailabilitySequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `AddCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>

### Assign/unassign feature

The assign/unassign feature allows the user to assign/unassign a worker to/from a role in a shift.

#### Implementation

This mechanism is facilitated by adding/deleting `Assignment` objects in the `McScheduler`. Each `Assignment` object
stores a `Shift`, `Worker` and `Role` object. The `McScheduler` maintains a `UniqueAssignmentList`, which enforces
uniqueness between `Assignment` objects by comparing them using `Assignment#isSameAssignment(Assignment)`.

![AssignmentClassDiagram](images/AssignmentClassDiagram.png)

The operations supporting the adding/deleting of `Assignment` objects are exposed in the `Model` interface as
`Model#addAssignment(Assignment)` and `Model#deleteAssignment(Assignment)`.

#### Example usage scenario

Step 1. The user executes `assign s/1 w/1 r/Cashier` to assign the 1st worker the role of a cashier in the 1st shift in
the McScheduler. The `assign` command creates an `Assignment` object, storing the 1st `Shift`, 1st `Worker` and cashier
`Role` objects. The command then checks if there already exists an `assignment` with the same `shift` and `worker` in
the model, as well as the `unavailability` of the `worker` to be assigned. If the `assignment` is unique and the
`worker` is available, the `assignment` is added to the list of `assignments` in the `model`.

![AssignSequenceDiagram](images/AssignSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `AssignCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>

Step 2. The user realises the previous command was a mistake and executes `unassign s/1 w/1` to unassign the 1st worker
from the 1st shift in the McScheduler. The `unassign` command creates a dummy `Assignment` object, storing the 1st
`Shift` and 1st `Worker` objects. The command then uses the dummy `assignment` as an identifier to identify the
`assignment` to be deleted from the list of `assignments` in the `model`.

### Role feature

The role feature allows users to add roles to a `Worker` and add `RoleRequirement` to a `Shift`. When assigning a `Worker` to a `Shift` under a particular `Role`,
we check that the `Worker` has the corresponding `Role` tagged to the `Worker` as the `Role` in the `RoleRequirement` of the particular `Shift`.

#### Implementation

The `Role` constructor takes in a `String` and creates a `Role` object with the according `roleName`. When tagging a `Worker` with a `Role`, we check that the `Role` is an allowed `Role` by checking if it exists in the 
`UniqueRoleList` of the McScheduler.

![RoleClassDiagram](images/RoleClassDiagram.png)

#### Commands
The following commands have been implemented to work with `Role`:
- `RoleAddCommand` to add new roles to `UniqueRoleList`
- `RoleDeleteCommand` to delete existing roles in `UniqueRoleList`
- `RoleListCommand` to list existing roles in `UniqueRoleList`

#### Example usage scenario

Step 1. The user executes `role-add cashier` to add the cashier role to the `UniqueRoleList` in the McScheduler `ModelManager`.
If the role being added already exists in the `UniqueRoleList`, `DuplicateRoleException` is thrown.

![AddRoleSequenceDiagram](images/AddRoleSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `RoleAddCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>


Step 2. The user realises the previous command was a mistake and executes `role-list` to get the index of the cashier role in the `UniqueRoleList`.
Using the role index in the `UniqueRoleList`, the user executes `role-delete ROLE_INDEX` to remove the cashier role from the `UniqueRoleList`.


### Take/cancel leave feature

The take/cancel leave feature allows users to set workers status to leave given a day and time. 
The setting prevents workers from being allocated to a work shift for which they are taking leave.

#### Implementation

The proposed mechanism to indicate that individuals are on leave makes use of the existing system for assigning workers
to shifts with a particular role. By making use of the existing assignment system, certain conflicts can be avoided:

- Assignment of a worker to a shift when they took leave for that shift will not result in two assignments to the same shift.

##### Leave

Leave is represented as an extension of `Role`. To prevent conflicts between `new Leave()` and `new Role("Leave")`,
these two objects are deemed equivalent through `Leave#equals()` and `Role#equals()`. This implementation should
be reconsidered if there should be a significant difference between these two objects.

![Leave Class Diagram](images/LeaveClassDiagram.png)

Due to their similarity, `Leave` objects are initiated using a common factory method as `Role` objects 
through `Role#createRole()`, which will parse the given input as a `Role` or a `Leave` respectively. 
This implementation prevents the creation of a role that has the same name as a leave.

##### Leave Assignment

Assignment makes use of the `Assignment` class features using `Leave` as the role.

##### Commands

Since `Leave` is essentially an extension of the assignment system, commands related to leave are very similar
to commands related to assignments.

- `TakeLeaveCommand` is a wrapper for `AssignCommand` and sets a worker to leave for specific shift. The following
diagram demonstrates how this works.

![TakeLeaveCommand Sequence Diagram](images/LeaveCommandsSequenceDiagram.png)

- `CancelLeaveCommand` is very similar to `UnassignCommand`. However, there is a need to check if the assignment being
removed represents a leave taken and not a normal role assignment. Hence, `CancelLeaveCommand` is implemented
separately and not as a wrapper. However, its implementation details is almost identical to `UnassignCommand`.

For more information, see [Implementation for Assign/Unassign Feature](#assignunassign-feature).

##### Mass Operation Commands

To increase the convenience of use for our expected typist user, we introduced a few mass operations related to leave:

- `TakeLeaveCommand` and `CancelLeaveCommand` both allow for many worker to one shift leave assignment similar to 
`AssignCommand` and `UnassignCommand`.
- `MassTakeLeaveCommand` and `MassCancelLeaveCommand` allow for one worker to many shift leave assignment.
  - These two mass commands allow for taking leave over a range of dates - similar to how leave is often planned 
  by workers.
  - These two commands do not require `Shift`s representing the datetime range to take leave to be present. A bare-bones 
  `Shift` with only `ShiftDate` and `ShiftTime` will be initialised for each `Shift` that has no identity equivalent 
  (via `Shift#isSameShift()`) `Shift` present in the McScheduler.
  - The two commands handle other `Assignment`s differently:
    - `MassTakeLeaveCommand` raises an error if the worker is already scheduled for a shift 
  (i.e. a non-leave `Assignment`).
    - `MassCancelLeaveCommand` searches for leaves in the datetime range and ignores non-leaves.
    
The following activity diagram describes the process behind `MassTakeLeaveCommand`.

![MassTakeLeaveCommand Activity Diagram](images/MassTakeLeaveActivityDiagram.png)

##### Future Extensions - Leave Quota

The following leave quotas could be implemented, possibly using the existing `RoleRequirement` class:

- Quota of leave per worker
- Quota of leave per shift


### MassOps Feature
For certain commands that will be frequently used (`assign`, `unassign`, `take-leave`, `cancel-leave`), mass
operations are supported to reduce the required number of command calls.
 
#### Implementation
These operations consist of their own `*Command` class and `*CommandParser` class. In each of the supported 
`*CommandParser class`, mass operations uses the `ArgumentMultimap#getAllValues(Prefix)` method, which parses the 
user input and returns all values that start with the specific prefix. In this case, the prefix is 'w/', 
signifying a `Worker`-`Role` relation.

Once the Command object has its `shiftIndex` and Set of `WorkerRole`, it creates individual `Assignment`s and adds
them to the Model.


![Class Diagram of AssignCommand, highlighting its MassOps](images/MassAssignClassDiagram.png)

#### Example Usage Scenario
Let's say that the manager has a new Shift, and requires 3 of their existing staff members to work on 
that shift immediately.

Step 1. The manager creates a new Shift through the `shift-add` command if it was not already done.

Step 2. The manager calls `assign` to assign the 3 existing Workers to the Shift. 
eg. `assign s/8 w/2 Cashier w/3 Fry Cook w/7 Janitor` to assign Workers 2, 3, and 7 to the Role of Cashier, Fry Cook, 
and Janitor respectively to Shift number 8.

Step 3. McScheduler parses the input and creates 3 Assignments: 
shift8-worker2-Cashier, shift8-worker3-Fry Cook, shift8-worker7-Janitor and adds them to the Model


![Object Diagram of one AssignCommand used to assign 3 workers into a shift](images/MassAssignObjectDiagram.png)

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* is a McDonald's shift manager
* manages a significant number of worker contacts
* manages a large number of work shifts
* prefers desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: McScheduler provides a one-stop solution for McDonald's shift managers' needs for shift scheduling and worker contact/compensation. All relevant information is easily available to help streamline the management process. It is also easy for the manager to contact workers and manage their hours and pay. McScheduler aims to be faster than a typical mouse/GUI driven app.


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                    | I want to …​                                                                         | So that I can…​                                                        |
| -------- | ------------------------------------------ | ------------------------------------------------------------------------------------ | ---------------------------------------------------------------------- |
| `* * *`  | user                                       | add a new worker                                                                     | track the worker's data and schedule their work                        |
| `* * *`  | user                                       | view a list of all workers                                                           | know who are the workers I can assign shifts to                        |
| `* * *`  | user                                       | view the contact details of workers                                                  | contact them easily                                                    |
| `* * *`  | user                                       | edit the details of a worker                                                         | have the most up-to-date information if I need to contact them         |
| `* * *`  | user                                       | assign roles to a worker                                                             | put them in shifts based on the work that they are trained to do       |
| `* * *`  | user                                       | delete a worker                                                                      | remove a worker who has left McDonald's                                |
| `* * *`  | user                                       | add a new shift                                                                      | assign workers to shifts                                               |
| `* * *`  | user                                       | view a list of all shifts                                                            | know which shifts need a worker                                        |
| `* * *`  | user                                       | add roles that need to be filled in a shift                                          | assign workers into those roles based on what is needed                |
| `* * *`  | user                                       | set the number of workers needed for each role in a shift                            | schedule workers based on what is needed                               |
| `* * *`  | user                                       | assign a worker to a shift                                                           | fill shift positions                                                   |
| `* * *`  | user                                       | edit the details of a shift                                                          | reflect any changes in the number of workers needed for the shift      |
| `* * *`  | user                                       | unassign a worker from a shift                                                       | find a replacement if the worker is no longer available for that shift |
| `* * *`  | user                                       | delete a shift                                                                       | remove an unwanted shift                                               |
| `* *`    | new user                                   | see a guide on how to use the key functions                                          | learn how to use the app                                               |
| `* *`    | user                                       | see a summary of the various commands                                                | easily refer to it when I forget the exact format for the commands     |
| `* *`    | user                                       | generate a weekly shift schedule                                                     | see at a glance the workers assigned to every shift in the week        |
| `*`      | user                                       | login                                                                                |                                                                        |
| `*`      | user                                       | see a worker's service rating                                                        | decide who to give more work opportunities to                          |


*{More to be added}*

### Use cases

(For all use cases below, the **System** is the `McScheduler` and the **Actor** is the `user`, unless specified otherwise).

#### Use case: Add a worker (UC-001)

**MSS**

1. User requests to add worker.
2. McScheduler adds worker.

   Use case ends.

**Extensions**
* 1a. The given worker information has missing or wrong data.

    * 1a1. McScheduler shows an error message.

      Use case ends.

#### Use case: Delete a worker (UC-002)

**MSS**

1. User requests to list workers.
2. McScheduler shows a list of workers.
3. User requests to delete a specific worker in the list.
4. McScheduler deletes the worker.

   Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. McScheduler shows an error message.

      Use case resumes at step 2.
  
#### Use case: Edit a worker's information (UC-003)

**MSS**

1. User requests to list workers.
2. McScheduler shows a list of workers.
3. User requests to edit a specific worker in the list.
4. McScheduler edits the worker.

   Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. McScheduler shows an error message.

      Use case resumes at step 2.

* 3b. No information is given or the information is invalid.

    * 3b1. McScheduler shows an error message.

      Use case resumes at step 2.

#### Use case: Add a shift (UC-004)

**MSS**

1. User requests to add a shift.
2. McScheduler adds the shift.

   Use case ends.

**Extensions**

* 1a. The shift information is missing or invalid (wrong values).

    * 1a1. McScheduler shows an error message.

      Use case ends.
 
#### Use case: Delete a shift (UC-005)

**MSS**

1. User requests to list shifts.
2. McScheduler shows a list of shifts.
3. User requests to delete a specific shift on the list.
4. McScheduler deletes the shift.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. McScheduler shows an error message.

      Use case resumes at step 2.

#### Use case: Edit a shift's information (UC-006)

**MSS**

1. User requests to list shifts.
2. McScheduler shows a list of shifts.
3. User requests to edit a specific shift in the list.
4. McScheduler edits the shift.

   Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. McScheduler shows an error message.

      Use case resumes at step 2.

* 3b. No information is given or the information is invalid.

    * 3b1. McScheduler shows an error message.

      Use case resumes at step 2.

#### Use case: Assign a worker to a shift (UC-007)

**MSS**

1. User requests to list shifts.
2. McScheduler shows a list of shifts.
3. User requests to list workers.
4. McScheduler shows a list of workers.
5. User requests to assign worker at a specific position on the workers' list to a shift at a specific position on the shifts' list.
6. McScheduler assigns specified worker to specified shift.

   Use case ends.
 
**Extensions**

* 2a. The list of shifts is empty.

  Use case ends.

* 4a. The list of workers is empty.

  Use case ends.

* 5a. At least one of the given indexes are invalid.

    * 5a1. McScheduler shows an error message.

      Use case resumes at step 4.
  
* 5b. The worker is unable to fulfil any role required for given shift.

    * 5b1. McScheduler shows an error message.

      Use case resumes at step 4.
 
#### Use case: Unassign a worker from a shift (UC-008)

**MSS**

1. User requests to list shifts.
2. McScheduler shows a list of shifts.
3. User requests to list workers.
4. McScheduler shows a list of workers.
5. User requests to unassign worker at a specific position on the workers' list to a shift at a specific position on the shifts' list.
6. McScheduler unassigns specified worker from specified shift.

   Use case ends.

**Extensions**

* 2a. The list of shifts is empty.

  Use case ends.

* 4a. The list of workers is empty.

  Use case ends.

* 5a. At least one of the given indexes are invalid.

    * 5a1. McScheduler shows an error message.

      Use case resumes at step 4.

* 5b. The worker is not assigned to the given shift.

    * 5b1. McScheduler shows an error message.

      Use case resumes at step 4.
  
#### Use Case: Hire a new worker for shifts (UC-009)

**MSS**

1. User <u>adds a worker (UC-001)</u>.
2. User <u>assigns worker to a shift (UC-007)</u>.

   Step 2 is repeated for all shifts the worker is hired for.

   Use case ends.

*{More to be added}*

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2.  Should be able to hold up to 1000 workers and 1000 shifts without a noticeable sluggishness in performance for typical usage.
3.  Should be able to save up to 1000 workers and 1000 shifts worth of data that persists over sessions.
4.  Data should be saved after every change.
5.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
6.  A fresh new user should be able to figure out how to use the app easily.

*{More to be added}*

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, macOS
* **Role**: A position that a worker is able to fill based on their skill set (e.g Cashier, Cleaner, Burger Flipper)
* **Service Rating**: A rating given based on how well the worker performs at their work

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a worker

1. Deleting a worker while all workers are being shown

   1. Prerequisites: List all workers using the `worker-list` command. Multiple workers in the list.

   1. Test case: `worker-delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `worker-delete 0`<br>
      Expected: No worker is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `worker-delete`, `worker-delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
