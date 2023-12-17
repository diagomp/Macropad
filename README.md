# Macropad

### Simple Ardunio based keyboard for shortcuts and macros.

This project consist of a simple keyboard that communicates via USB to a controller application. The keyboard sends information of the key that has been pressed and how. With that, the controller application manages the actions it need to execute based on the user configuration.

The use of a controller application has some advantages in comparisong of directly executing commands througth the Arduino. Some of the key aspects of this project are:

* Any user can configure the actions assigned to any button without the need of reprogramming the Arduino.
* The controller aplication can store different configuration profiles. So, the user can alternate the current profile depending on the application he/she is using.
* Any button in the keyboard has two possible actions -- *short press* and *long press*.
* It is possible to use also encoders, wich adds two more interaction to that position of the grid in the keyboard -- *clockwise (CW)* and *counter-clockwise (CCW)* rotation.
* The controller application can be hidden while not using. It runs on the System Tray, so the interface can be recover at any moment.

![Macropad screenshot](Macropad.PNG)

## How to use it



## Dependencies

The ibrary [jSerialComm](https://fazecast.github.io/jSerialComm/) is required to use this project.


## License

This project is [MIT Licensed](LICENSE).
