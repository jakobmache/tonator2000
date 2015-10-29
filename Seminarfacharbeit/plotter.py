import matplotlib.pyplot as plt

# Pfad der Ausgabedatei vom Java-Programm
path = "C:\\Users\\Jakob\\git\\semi\\Seminarfacharbeit\\envelopeAusgabe.txt"

with open(path) as fileObj:
    content = fileObj.readlines()
plt.plot(content)
plt.show()
