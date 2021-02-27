![app_icon](https://forge.iut-larochelle.fr/uploads/-/system/project/avatar/1955/ic_launcher.png?width=64)
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