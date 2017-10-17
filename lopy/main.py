from network import Bluetooth
from network import WLAN
#from network import socket
import pycom
import time
import machine
import binascii
import gc

beaconlist = ['b9407f30-f5f8-466e-aff9-25556b57fe6d']
beaconevents = []
timelastdata = time.time()

def new_adv_event(event):
    global beaconlist, beaconevents, timelastdata
    if event.events() == Bluetooth.NEW_ADV_EVENT:
        anydata = True
        while anydata:
            adv = bluetooth.get_adv()
            if adv != None:
                timelastdata = time.time()
                devid = binascii.hexlify(adv[0]).decode('utf-8')
                print(adv[0])
                rssi = str(adv[3]*-1)
                if devid in beaconlist:
                    if len(beaconevents) > 5:
                        beaconevents.pop(0)
                        print(devid)
                    beaconevents.append([devid, rssi])
            else:
                anydata = False

print('Starting BLE scan')
bluetooth = Bluetooth()
bluetooth.callback(trigger = Bluetooth.NEW_ADV_EVENT, handler = new_adv_event)
bluetooth.init()
bluetooth.start_scan(-1)

cycles = 0
p_in = machine.Pin('G17',machine.Pin.IN, pull=machine.Pin.PULL_UP)

while True:
    if p_in() == 0:
        print('pin')
        bluetooth.stop_scan()
        break

    cycles += 1
    # Run garbage collector every 20 cycles.
    if cycles%20 == 0:
        gc.collect()
    # If no BLE event for 10 seconds, hard reset
    if time.time() - timelastdata > 10:
        machine.reset()



#s = socket.socket()
