# Android Project 4th Semester
**Class:** _IT-AND1-A21_

**Student:** _Viggo Petersen_

**App name:** _VIA Events_

**Youtube video:** [https://youtu.be/9_JTU0bM4Xk](https://youtu.be/9_JTU0bM4Xk)

# Abstract
VIA Events is an app to arrange events at the new VIA University College. It should allow users to create and view events meant for VIA students, at or outside of the University. The events can be public so all students can see and attend the event, or private so only invited users can attend. A possible feature would be to have a visible map of all the rooms at campus, so Students can easily locate the event, and with that the ability to book rooms with VIA.

# Requirements
The project has the following requirements.
(Some of the requirements have been striked through because I could not find a solution for them. No public API or information available)

| Requirement | Priority | Complete |
|-------------|----------|----------|
| Users can log in| M | Yes |
| Users can CRUD events | M | Partly |
| Users can see a list of public events | M | Yes |
| Events can be private that have guest lists | S | Partly |
| Users can RSVP to events | S | Yes |
| Events can have tags ie. talks, party, workshop | S | Yes |
| ~~Users can book rooms with VIA~~ | C | No |
| App has a display mode to be shown on TV | C | No |
| ~~App shows a map of the school~~ | W | No |
| ~~Users can see which rooms are available~~ | W | No |

## Missing features
* For the requirement "Users can CRUD events" it is only possible to Create and Read event, not delete or update
* For the requirement "Events can be private that have guest lists" it is possible to set an event to private, but there's no way to add people to it, so it does nothing.
* It is not possible to actually book rooms at VIA as I couldn't find an API for it
* It is not possible to see a map of the school as I could not find one
* It is not possible to see if rooms are available as VIA has no API for it
* Images are scaled to a hardcoded size to save bandwidth
* It is not possible to remove attendance from an event
