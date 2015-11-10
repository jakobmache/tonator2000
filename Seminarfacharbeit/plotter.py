import matplotlib.pyplot as plt

# Pfad der Ausgabedatei vom Java-Programm
path = "C:\\Users\\Jakob\\git\\semi\\Seminarfacharbeit\\ausgabeEnvelope.txt"

with open(path) as fileObj:
    content = fileObj.readlines()
    print len(content)
plt.plot(content)
plt.plot((4410, 4410), (0, 2), 'k-')
plt.plot((8820, 8820), (0, 2), 'k-')
plt.plot((13230, 13230), (0, 2), 'k-')
plt.show()
