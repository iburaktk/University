import sys
inputName = sys.argv[1]
outputName = sys.argv[2]
inputFile = open(inputName, "r")
messages = {}
for line in inputFile.readlines():
    line = line.strip("\n") # removing \n
    messageID = int(line.split("\t")[0])
    packetID = line.split("\t")[1]
    messagePart = line.split("\t")[2]
	# updating message
    if messageID in messages.keys():
        messageX = messages.get(messageID)
    else:
        messageX = {}
    messageX[packetID] = messagePart
    messages[messageID] = messageX
for i in range(0,2):
    message_number = 0
    if i == 1:
        sys.stdout = open(outputName, "w+")  # write all outputs to the file
    for messageID in sorted(messages):
        message_number += 1
        print("Message", message_number)
        for number in sorted(messages[messageID]):
            print(messageID, number, messages.get(messageID)[number], sep="\t")
#İbrahim Burak Tanrıkulu
