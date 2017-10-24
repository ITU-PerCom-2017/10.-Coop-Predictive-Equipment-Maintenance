
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

chrono = Timer.Chrono()

wlan = WLAN(mode=WLAN.STA)
nets = wlan.scan()

bluetooth = Bluetooth()
bluetooth.start_scan(-1)
adv = None
chrono.start()
while True:

    adv = bluetooth.get_adv()
    if adv != None:
        print(adv, '\n')
        #print(adv.data, '\n')
        #print(binascii.hexlify(adv.data).decode("utf-8"), '\n')
        #print(bluetooth.resolve_adv_data(adv.data, Bluetooth.ADV_MANUFACTURER_DATA), '\n')


    lap = chrono.read()
    if lap > 2:
        print("breaking")
        bluetooth.stop_scan()
        break
pycom.rgbled(0x7f0000)

for net in nets:
     if net.ssid == 'cooppifi':
         print('rpi found!')
         wlan.connect(net.ssid, auth=(net.sec, 'cooppifi2017'), timeout=5000)
         for _ in range(60):
             time.sleep(1)
             if wlan.isconnected():
                 print("connected to rpi")
                 break
             print('.', end='')

         else:
             print("could not connect")

            #while not wlan.isconnected():
            #    pycom.rgbled(0x7f0000)
            #    machine.idle() # save power while waiting
            #print('WLAN connection succeeded!')
            #break
time.sleep(2)
pycom.rgbled(0x7f7f00) # yellow
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
#address = ("10.42.0.1", 6789)
#s.connect(socket.getaddrinfo('10.42.0.1', 6790)[0][-1])
data = b'fedePenis'
#data = b'hello tcp'

time.sleep(2)

print("sending")
for _ in range(10):
    s.connect(socket.getaddrinfo('10.42.0.1', 6790)[0][-1])
    pycom.rgbled(0x007f00)
    time.sleep(0.5)
    pycom.rgbled(0x000000) # turn off led
    time.sleep(0.5)
    print('.')
    s.send(data)
    s.close()




#s.close()
#s.sendto(key, address)


#s = socket.socket()
