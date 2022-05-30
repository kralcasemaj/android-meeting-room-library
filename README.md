This example shows how to use an MVVM design with Architecture Components to call a JSON endpoint 
and display the data.

The app shows a list of floors, from which you can select a meeting room, which will show the available
time-slots for that room.

The library is intended to be used via the public methods in MeetingRoomViewModel - you can see how it
is supposed to be called in the FloorListFragment.

The library caches the floors, and will show the cached list if the device does not have internet access.

The data layer, as well as the view model, is provided within the library, with unit tests provided
for the MeetingRoomAPI and MeetingRoomViewModel.

The app only contains the view presentation layer, and includes an example of an Espresso UI test.

The library and the app can be built as separate modules.  If you want to run the app from Android
Studio, make sure you select 'app' as the module to run, and not 'androidlibraryexample'.  If you
want to build the library you should run the 'assembleDistribution' Gradle task which will also copy
the generated .aar file across to the 'app'.