
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

lopyID = 2 # set this unique to each lopy
print('started! lopy id: ', lopyID)
#for breaking out of while(True) loops. Removed when program is finished
chrono = Timer.Chrono() #
chrono.start()

wlan = WLAN(mode=WLAN.STA)
nets = wlan.scan()

bluetooth = Bluetooth()
bluetooth.start_scan(-1)
adv = None # bluetooth advertisement

pycom.rgbled(0x7f0770)
time.sleep(0.5)
pycom.rgbled(0x000000) # turn off led
time.sleep(1)
pycom.rgbled(0x7f0770)
time.sleep(0.5)
pycom.rgbled(0x000000) # turn off led
time.sleep(1)

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
#pycom.rgbled(0x7f7f00) # yellow

time.sleep(2)

def sendToSocket(beaconID, rssi):
    RSSIpositive = rssi * -1
    #newData = beaconID + lopyID + RSSIpositive
    #newData = 255
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect(socket.getaddrinfo('10.42.0.1', 6792)[0][-1])
    pycom.rgbled(0x007f00)
    time.sleep(0.5)
    pycom.rgbled(0x000000) # turn off led
    time.sleep(0.5)
    print('.')
    data = bytearray()
    #data.append(newData)
    data.append(1)
    data.append(2)
    data.append(3)
    print("sending to socket, binary: ", data, data[0])
    #conv = binascii.hexlify(data)
    #print(conv, encoding='utf8')
    s.send(data)
    s.close()

# b'f3bc0b6feb4c' # raw: b'\xf3\xbc\x0bo\xebL'
# b'edbed48d0b87' # raw: b'\xed\xbe\xd4\x8d\x0b\x87
# b'c7c383eeaaa4' # raw?
# b'cd87e6a38dc2' # raw?
print('!')
while True:
    pycom.rgbled(0x007f00) # green
    adv = bluetooth.get_adv()

    if adv != None:
        #print('!!')
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
            sendToSocket(beaconID = 4, rssi = adv[3]) # adv[3]
    #time.sleep(1)

    #lap = chrono.read() # for breaking out of loop (remove this part when program is finished)
    #if lap > 30:
    #    print("breaking")
    #    bluetooth.stop_scan()
    #    break
