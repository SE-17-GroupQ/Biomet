# Biomet (Multiple Face Recognition)
## Implemented a method of taking attendance using the android application.
 - The use of Kairos API for face recognition and detection.
 - The UI made is very simple and it has a user friendly understanding with just two buttons one to enroll and another to verify.
 - In the application usage of a database is done to keep the track of the attendance of the students.

## How to Register
 - Click on Enroll if you are using for the first time i.e Registering your details to the database both the SQLite as well as Kairos
 - The person on clicking the Enroll Button will come up with a pop-up window which will ask for the Unity-Id(unique id for student at NC State) and their registration photo.
 
## How to verify/take attendance
 - In this part there is button named 'Recognize' in the application, which on clicking the default camera application to grab the photo and with the API's usage we can get the students in the photo and mark them present in the lecture.
 - The list of all the students who are registered in the database and matched in the photograph are been marked present.
 
## Steps to be followed to use the application
### Enrollment Process
 - 1-Click on Enroll a popup window will open.
 - 2-When the popup opens you will have to add your Unity ID and Clicking on the 'ENROLL' button in the popup a camera will open.
 - 3-Go in the selfie mode and enroll/register your face in the database
 - 4-You will get a success message when enrolled or some error if there is a connection problem.
 - This is just a one time enrollment process.
 - We have added a condition that only once a Unity ID can be enrolled. If someone enrolls again he/she will be given the message that the person is already enrolled.
### Recognization Process
 - 1-Click on the Recognize button
 - 2-A camera will open up and you can either take a selfie of the group or else someone can take a photo with the back camera.
 - 3-After the recognition process all the person which are recognized will be shown as present.
 - If someone not registered will be shown with a message of 'No Match Found'
