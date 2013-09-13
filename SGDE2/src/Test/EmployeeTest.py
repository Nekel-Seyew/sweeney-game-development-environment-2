from Test import JythonEmployeeType
from Test import AnotherTest

class Employee(JythonEmployeeType):
    def __init__(self, first, last, id):
        self.first=first
        self.last=last
        self.id=id
        self.location=Address("Home")

    def getEmployeeFirst(self):
        return self.first
    def getEmployeeLast(self):
        return self.last
    def getEmployeeId(self):
        return self.id
    def getAddress(self):
        return self.location
        