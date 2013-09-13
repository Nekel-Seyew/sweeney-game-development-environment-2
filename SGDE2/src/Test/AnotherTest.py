#! /usr/bin/python

# To change this template, choose Tools | Templates
# and open the template in the editor.
import java.lang.Object

class Address(java.lang.Object):
    def __init__(self, location):
        self.location=location
        
    def getLocation(self):
        return self.location
