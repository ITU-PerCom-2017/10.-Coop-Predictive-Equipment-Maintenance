import time
import pycom
from machine import I2C
from struct import unpack
from network import Bluetooth
from network import WLAN
bt = Bluetooth()
bt.set_advertisement(name="Fellix")
off = 0x000000
red = 0xff0000
green = 0x00ff00
blue = 0x0000ff
minMoist = 380
pycom.heartbeat(False)
pycom.rgbled(green)
print('starting main')
#setting up class for connecting to wifi
class WifiConnect:
    print('Connecting to WLAN')
    wlan = WLAN()
    wlan.mode(WLAN.STA)
    #scanning networks
    nets = wlan.scan()
    #one by one
    for net in nets:
        if net.ssid == 'sensors':
            print('Network found!')
            wlan.connect(net.ssid, auth=(net.sec, 'n0n53n53'), timeout=5000)
            while not wlan.isconnected():
                print('Wlan not connected')
                time.sleep(0.5)
            print('WLAN connection succeeded!')
            print('Connected to with IP address: ' + wlan.ifconfig()[0])
            break
#setting up class for handling sensors
class Chirp:
    print('starting Chirp')
    def __init__(self, address):
        self.i2c = I2C(0, I2C.MASTER, baudrate=10000)
        self.address = address
    def get_reg(self, reg):
        val = unpack('<H', (self.i2c.readfrom_mem(self.address, reg, 2)))[0]
        return (val >> 8) + ((val & 0xFF) << 8)
    def moist(self):
        return self.get_reg(0)
class Beacon:
    print('starting Beacon')
    while True:
        moister = Chirp(0x20).moist()
        print(moister)
        time.sleep(10)
        if moister < minMoist:
            print('plant is thirsty')
            bt.advertise(True)
            pycom.rgbled(red)
            time.sleep(0.2)
            pycom.rgbled(off)
            time.sleep(0.6)
        if moister > minMoist:
            bt.advertise(False)
            pycom.rgbled(off)

WifiConnect()
Beacon()
