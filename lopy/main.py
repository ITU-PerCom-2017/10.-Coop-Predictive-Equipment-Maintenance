
from network import Bluetooth
from network import WLAN
import socket
#from network import socket
import pycom
import time
import machine
import binascii
from machine import Timer
pycom.heartbeat(False)
lopyID = 1 # set this unique to each lopy

#for breaking out of while(True) loops. Removed when program is finished
chrono = Timer.Chrono() #
chrono.start()

wlan = WLAN(mode=WLAN.STA)
nets = wlan.scan()

bluetooth = Bluetooth()
bluetooth.start_scan(-1)
adv = None # bluetooth advertisement

pycom.rgbled(0x7f0000)

for net in nets: # connect to rpi wifi
     if net.ssid == 'cooppifi':
         print('rpi found!')
         wlan.connect(net.ssid, auth=(net.sec, 'cooppifi2017'), timeout=4000)
         for _ in range(20):
             time.sleep(1)
             if wlan.isconnected():
                 print("connected to rpi")
                 break
             print('.', end='')
         else:
             print("could not connect to wifi, resetting lopy...")
             machine.reset()
pycom.rgbled(0x7f7f00) # yellow

#address = ("10.42.0.1", 6789)
#s.connect(socket.getaddrinfo('10.42.0.1', 6790)[0][-1])

#data = b'hello tcp'

time.sleep(2)

def sendToSocket(beaconID, rssi):

    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect(socket.getaddrinfo('10.42.0.1', 6790)[0][-1])
    pycom.rgbled(0x007f00)
    time.sleep(0.5)
    pycom.rgbled(0x000000) # turn off led
    time.sleep(0.5)
    print('.')
    data = bytearray()
    data.append(lopyID)
    data.append(beaconID)
    data.append(rssi)
    print("sending to socket: ", data)
    s.send(data)
    s.close()

# b'f3bc0b6feb4c' # raw: b'\xf3\xbc\x0bo\xebL'
# b'edbed48d0b87' # raw: b'\xed\xbe\xd4\x8d\x0b\x87
# b'c7c383eeaaa4' # raw?
# b'cd87e6a38dc2' # raw?
while True:
    adv = bluetooth.get_adv()
    if adv != None:
        macRaw = adv[0] # binary mac address
        macReal = binascii.hexlify(macRaw) # convert to hexadecimal
        if macReal == b'f3bc0b6feb4c':
            print('beacon A rssi: ', adv[3]) # adv[3] is rssi
            sendToSocket(beaconID = 1, rssi = adv[3])
        elif macReal == b'edbed48d0b87':
            print('beacon B rssi: ', adv[3])
            sendToSocket(beaconID = 2, rssi = adv[3])
        elif macReal == b'c7c383eeaaa4':
            print('beacon C rssi: ', adv[3])
            sendToSocket(beaconID = 3, rssi = adv[3])
        elif macReal == b'cd87e6a38dc2':
            print('beacon D rssi: ', adv[3])
            sendToSocket(beaconID = 4, rssi = adv[3])

    lap = chrono.read() # for breaking out of loop (remove this part when program is finished)
    if lap > 30:
        print("breaking")
        bluetooth.stop_scan()
        break
