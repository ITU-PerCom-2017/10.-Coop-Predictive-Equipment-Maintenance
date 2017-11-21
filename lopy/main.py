
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
pycom.rgbled(0x000000) # turn off led
port = 6791

lopyID = 3 # set this unique to each lopy
print('started! lopy id: ', lopyID, ' port: ', port)
#for breaking out of while(True) loops. Removed when program is finished
chrono = Timer.Chrono() #
chrono.start()

wlan = WLAN(mode=WLAN.STA)
nets = wlan.scan()

pycom.rgbled(0x7f0770)
time.sleep(0.5)
pycom.rgbled(0x000000) # turn off led
time.sleep(1)
pycom.rgbled(0x7f0770)
time.sleep(0.5)
pycom.rgbled(0x000000) # turn off led
time.sleep(1)
pycom.rgbled(0x7f0770)
time.sleep(0.5)
pycom.rgbled(0x000000) # turn off led
time.sleep(1)
#pycom.rgbled(0x7f0770)
#time.sleep(0.5)
#pycom.rgbled(0x000000) # turn off led
#time.sleep(1)
#pycom.rgbled(0x7f0000) # red
#time.sleep(0.5)
#pycom.rgbled(0x000000) # turn off led
#time.sleep(1)

#pycom.rgbled(0x7f0000)

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

bluetooth = Bluetooth()
bluetooth.start_scan(-1)
adv = None # bluetooth advertisementa


time.sleep(1)
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM, socket.IPPROTO_UDP)
s.connect(socket.getaddrinfo('10.42.0.1', port)[0][-1])

def sendToSocket(beaconID, rssi):
    RSSIpositive = rssi * -1
    data = bytearray()
    data.append(lopyID)
    data.append(beaconID)
    data.append(RSSIpositive)
    s.send(data)

# new mac adresses:
#C7:C3:83:EE:AA:A4
#FB:6F:11:25:79:9B
#F7:F0:BB:A6:ED:F7
#C3:C7:E7:73:7C:3F

# b'f3bc0b6feb4c' # raw: b'\xf3\xbc\x0bo\xebL'
# b'edbed48d0b87' # raw: b'\xed\xbe\xd4\x8d\x0b\x87
# b'c7c383eeaaa4' # raw?
# b'cd87e6a38dc2' # raw?

#pycom.rgbled(0x007f00) # green
while True:
    pycom.rgbled(0x007f00)
    advList = bluetooth.get_advertisements()
    for adv in advList:
        macRaw = adv[0] # binary mac address
        macReal = binascii.hexlify(macRaw) # convert to hexadecimal
        if macReal == b'fb6f1125799b': # old: b'f3bc0b6feb4c':
            print('beacon A rssi: ', adv[3]) # adv[3] is rssi
            sendToSocket(beaconID = 1, rssi = adv[3])
        elif macReal == b'f7f0bba6edf7': # old: b'edbed48d0b87':
            print('beacon B rssi: ', adv[3])
            sendToSocket(beaconID = 2, rssi = adv[3])
        elif macReal == b'c7c383eeaaa4': # <-- old
            print('beacon C rssi: ', adv[3])
            sendToSocket(beaconID = 3, rssi = adv[3])
        elif macReal == b'c3c7e7737c3f': # old: b'cd87e6a38dc2':
            print('beacon D rssi: ', adv[3])
            sendToSocket(beaconID = 4, rssi = adv[3]) # adv[3]
    #time.sleep(1)
s.close()
    #lap = chrono.read() # for breaking out of loop (remove this part when program is finished)
    #if lap > 30:
    #    print("breaking")
    #    bluetooth.stop_scan()
    #    break
