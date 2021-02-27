![app_icon](https://i.ibb.co/F7MB6Wb/ic-launcher-1.png)
# Objectif Sport

#### **Functional Context**
Sport monitoring Android application for sportsmen and women. 
This application allows you to define sports, sport's objectives (time or distance with or without deadline) and activities to achieve them. 

#### **Developper** 
_Alfred Gaillard_ (contact@alfred-gaillard.fr). 

#### **Standards**
- **Languages used** : English for everything.
- **Naming Convention** : https://www.oracle.com/java/technologies/javase/codeconventions-namingconventions.html && https://source.android.com/setup/contribute/code-style#follow-field-naming-conventions.
- **Git** : GitFlow type.
- **Devices** : Android smartphones.
- **Api used** : Api 23 -> 30 to get more than 80% of devices. 

#### **Libs used**
- **TimePicker** : [time-duration-picker](https://github.com/svenwiegand/time-duration-picker).
- **Map** : [MapBox](https://docs.mapbox.com/android/maps/guides).
- **Data Storage** : [gson](https://github.com/google/gson) (once serialized, objects are then placed in the SharedPreferences for saving and deserialized for loading them). 

#### **Testing**
- Use of Espresso test to test the bases of the interface. 

#### **Get Started**
1. Create a sport into the "My Sports" tab by clicking on the "+" button.
2. Add a goal into the "My Goals" tab. 
3. Add your activities into "My Activities" tab.
4. When you doing some sport, just click on your activity to make it progress.

#### **Known bugs**
There are no known bugs to actually. However, I have problems with the map when I'm on emulator but I think it's my environment. However if you encounter a crash when opening the map, feel free to contact me and test on a real device.

#### **Devices used for testing**
I recently encountered problems with mapbox on emulators (not only on this project).So all recent testing was done with a real device, a OnePlus 7T 256GB with Android 10 and 411dp screen.
Until recently the tests were done with the following emulators:
- Pixel 3a XL api 23 & 30 (400Dp)
- Pixel a3 api 23 & 30 (440Dp)
- Pixel XL api 23 & 30 (560Dp)
- Nexus 5X api 23 & 30 (420Dp)
