# Study Session

This is the primary repo for the android application Study Session. This application is an academic hub where users can find study groups based on subject matter and available times.

## Development

The development of this application is broken up into specific activities and features. Each contributor is responsible for completing their agreed portion of the application
  * David Curtis: User Acount Registration, User login, cross activity data communication, data input validation/security 
  * Terry Woosley: Study Group management and main activity population
  * Ronny Aretz: Study Group creation/join/searching and management
  ** DISCLAIMER Ronny does not show up as a contributor due to a git error, but his contributions can be clearly seen in commit history
  * Dylan Rongey: User profile page design

## Data Stroage

This application uses Cloud Firebase for all back-end services

### Local Persistant Data
 * User login information will persist on the local machine via SharedPreferences to allow auto login feature
 
### Remote Firebase Data
 * User's available study time's and joined study groups
 * Study Group's name, time, creator, members, and other attributes relating to the groups
 
## Testing Credentials

Use the following account information to login into the application (Testing purposes only)

 * Email: test@user.com
 * Password: Password123!

## Sequence Information
 * Login to the application using the above testing credentials
 * Press the profile button to view and edit the user's profile information or logout
 * Press on the create group button to create a new study group
 * Press on the search groups button to search for study groups
 
## Supported Devices
This application can run on all Adroid devices running Nougat OS or newer

## APK
* App APK can be found in debug folder

## Built With

* [Firebase](https://firebase.google.com/) - Database System
* [Gradle](https://gradle.org/) - Dependency Management

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

