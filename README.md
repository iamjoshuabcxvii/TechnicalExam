# Technical Exam Assumptions

Assumptions:

Ø Unit test is required, but there is no mention of code coverage and hence unit test for the complex functions will be covered only for as part of smoke testing will be implemented with disregard to the code coverage.

Ø Registration of users or admins is not required, and hence admins and users were all pre-populated already with registration assumed to be handled by another program/interface/API that would then populated the users table in the database. Duplicate registration of same numbers is assumed to be not allowed too, hence there will be no duplicate mobile numbers on the database.

Ø Columns K to Z, and Row 27 and up will all error out on booking which ever comes first

Ø If one or more of the booked seats is invalid or doesnt exist already, it will error out the entire transaction and throw out an error and make the user to retry the booking again by going back to the main user main menu display

Ø A differentiation was made in the naming convention of the table. The table is named User, with two role types, admin and customer (user). In which case User is defined as a user of any role in the application, and the admin is the administrator of the application, and customer is the user to which does the booking.

Ø Ticket number will be formated as: BookingNo-<SHOW_NUMBER><SEAT_NUMBER>-<timestamp of the booking in epoch>

○ Example: BookingNo-1A1_1702612800

Ø There are only a few bookings that could be done for a single show anyway and hence an epoch was used as a unique key for generating the ticket number, this will also make it easy to determine when was the booking made when epoch is converted to human readable date.

Ø In order to be able to book a new seat on the show with bookings already, the user had to cancel all the bookings made on that particular show and rebooked it with the additional or reduced number of seats in order to comply with the requirement of only one bookings per user per show.

Ø the password was not encrypted for ease of implementation, an encrypted password was not part of the requirements afterall, but could be for enhancement.

Ø Primary key used for Users table is auto-generated number, and this will be used as a unique identifier per user on their assumed registration from another API, it is assumed here that the user can change his/her name, hence the username was not used as a primary key.

Ø The ability to mark the show as active or not is not implemented as this was not part of the requirement, and hence could be for enhancement in the future.

Ø Cancellation window when adding a show was made optional, but the application will default it to 2 minutes if nothing was inputted in there for ease of use.

Ø This is probably for a local cinema and hence bookings aren't that frequent. Hence calls to the database were made frequent for improved UX experience too since inputs were already being validated real time.


