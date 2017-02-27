# Life Collage

#### _DevelopmentNow Velocity Program, 02.24.2017_

#### By Sheena Do & Caleb Stevenson

Life Collage is the result of DevelopmentNow's Velocity program. Velocity is a 5 week incubator for Junior Developers. In this session of the Velocity program, Caleb Stevenson and Sheena Do decided to build Life Collage, a picture lifestyle app. 

Life Collage allows users to take pictures and add them to a collage. There are no tags, captions or comments, because we believe the pictures should speak for themselves. Users can also search for other users and view their collages. 

Now, we've all seen this type of app before. The thing that sets Life Collage apart is the ability to pass collages from one user to another. This means if Kate passes her collage to Josh, she is entrusting its care to him. Josh now becomes the owner of that collage. Josh can add pictures to the collage until he decides to pass it off to Abby. And so on and so forth. 

The other interesting thing about passing is that since it uses Bluetooth, you actually have to be close enough to the other user in order to pass the collage. We think this creates an interesting tie to time and space that other sharing methods can't create.

Life Collage is available for free download in the [Google Play Store](https://play.google.com/store/apps/details?id=com.doandstevensen.lifecollage).

## Prerequisites

You will need the following things properly installed on your computer.

* [Android Studio](https://developer.android.com/studio/index.html)

## Installation

* `git clone https://github.com/sheenanick/life-collage`
* Open Android Studio
* Select 'Open an existing Android Studio project' and find the cloned repository

## Running / Development

* Create a virtual device in Android Studio and run the application
* Or connect an Android device to your computer and run the application on the device

## Known Bugs

* Pictures taken on certain Android devices are rotated 90 degrees when saved.
* Bluetooth connectivity may be unstable. For best results follow these steps in order:<br>
  1) Device receiving the collage must first make itself discoverable by pressing the "PASS TO ME!" button.<br>
2) Device sending the collage must then scan for nearby devices by pressing the "LOOK AROUND!" button.<br>
3) Select the receiving device from the list, and this will establish a connection with that device.<br>
4) Select the collage you wish to pass from the dropdown menu, and press the "PASS" button.<br>
5) A dialog will pop up on the receiving device if the collage has been received successfully.

## Support and Contact Details

Please feel free to contact Sheena Do(sheenanick@gmail.com) or Caleb Stevenson(cgrahamstevenson@gmail.com) if you have any issues, questions, ideas or concerns.

### License

This software is licensed under the MIT license.

Copyright (c) 2017 **_Sheena Do & Caleb Stevenson_**
