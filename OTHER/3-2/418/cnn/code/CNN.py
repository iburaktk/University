import os
import numpy as np
from PIL import Image
from matplotlib import pyplot as plt
from time import time
import pickle

import torch
from torch import nn
from torch.utils.data import DataLoader
import torchvision
from torchvision import transforms, datasets, models
from torchsummary import summary

from sklearn.metrics import confusion_matrix

if torch.cuda.is_available():
    deviceStr = "cuda"
else:
    deviceStr = "cpu"
device = torch.device(deviceStr)
print("Device selected:",deviceStr)

def main():

    transform = transforms.Compose([
        transforms.RandomHorizontalFlip(p=0.25),
        transforms.RandomRotation(degrees=30),
        transforms.Resize(225),
        transforms.CenterCrop(180),
        transforms.ToTensor(),
    ])


    if os.path.isfile("trainDataset.pkl") is True:
        train_dataset = pickle.load(open("trainDataset.pkl","rb"))
        validate_dataset = pickle.load(open("validateDataset.pkl","rb"))
        test_dataset = pickle.load(open("testDataset.pkl","rb"))
        print("Dataset loaded: ",
            len(train_dataset),"train images,", 
            len(validate_dataset),"validate images and",
            len(test_dataset),"test images.")
    else:
        dataset_path = '../Dataset'
        dataset = torchvision.datasets.ImageFolder(dataset_path, transform=transform)
        dataset_len = len(dataset)
        print("There are",dataset_len,"images in dataset.")
        lengths = []
        lengths.append(int(dataset_len*0.65))
        lengths.append(int(dataset_len*0.15))
        lengths.append(int(dataset_len*0.2))
        while (lengths[0] + lengths[1] + lengths[2] != len(dataset)):
            lengths[0] += 1 
        train_dataset, validate_dataset, test_dataset = torch.utils.data.random_split(dataset, lengths)
        print("Dataset splitted into",
            len(train_dataset),"train images,", 
            len(validate_dataset),"validate images and",
            len(test_dataset),"test images.")
        pickle.dump(train_dataset, open("trainDataset.pkl", "wb"))
        pickle.dump(validate_dataset, open("validateDataset.pkl", "wb"))
        pickle.dump(test_dataset, open("testDataset.pkl", "wb"))
        

    # We have 4 different batch size. Thus there are 12 different data loaders
    batchSizeArray = [16, 32, 175, 300]
    dataLoaders = []
    for i,batchSize in enumerate(batchSizeArray,0):
        dataLoaders.append([])
        dataLoaders[i].append(DataLoader(
            dataset=train_dataset,
            batch_size=batchSize,
            num_workers=4
        ))
        dataLoaders[i].append(DataLoader(
            dataset=validate_dataset,
            batch_size=batchSize,
            num_workers=4
        ))
        dataLoaders[i].append(DataLoader(
            dataset=test_dataset,
            batch_size=batchSize,
            num_workers=4
        ))

    
    myModels = []
    number = 0
    learningRates = [0.01, 0.001, 0.0005, 0.0001]
    for modelNum in range(2):
        for i,batchSize in enumerate(batchSizeArray,0):
            train_dataloader = dataLoaders[i][0]
            validate_dataloader = dataLoaders[i][1]
            for lr in learningRates:
                myModels.append(trainEvaluatePlot(30,train_dataloader,validate_dataloader,modelNum,lr,batchSize))
      
    model1 = myModels[13]
    model2 = myModels[29]

    test_dataloader = dataLoaders[3][2]
    predictions, trueLabels = Test(test_dataloader,model1)
    predictions = predictions.argmax(dim=1)
    drawConfusionMatrix(trueLabels,predictions,test_dataset.dataset.classes)

    predictions, trueLabels = Test(test_dataloader,model2)
    predictions = predictions.argmax(dim=1)
    drawConfusionMatrix(trueLabels,predictions,test_dataset.dataset.classes)


    resNet18 = models.resnet18(pretrained=True)
    for parameter in resNet18.parameters():
        parameter.requires_grad = False     # Freezing all convolutional layers
    numberOfFeatures = resNet18.fc.in_features
    resNet18.fc = nn.Linear(numberOfFeatures, 16)
    resNet18 = resNet18.to(device)

    train_dataloader = dataLoaders[3][0]
    validate_dataloader = dataLoaders[3][1]
    test_dataloader = dataLoaders[3][2]
    trainEvaluatePlot(30,train_dataloader,validate_dataloader,2,resNet18=resNet18)
    
    test_dataloader = dataLoaders[3][2]
    predictions, trueLabels = Test(test_dataloader,resNet18)
    predictions = predictions.argmax(dim=1)
    drawConfusionMatrix(trueLabels,predictions,test_dataset.dataset.classes)
    
    
    resNet18 = models.resnet18(pretrained=True)
    for parameter in resNet18.parameters():
        parameter.requires_grad = False     # Freezing all convolutional layers for now
    numberOfFeatures = resNet18.fc.in_features
    resNet18.fc = nn.Linear(numberOfFeatures, 16)
    resNet18 = resNet18.to(device)

    for name, child in resNet18.named_children():
        print(name)

    for name, child in resNet18.named_children():
        if name == "layer4":
            for parameter in child.parameters():
                parameter.requires_grad = True
            print("Last layer has been unfrozen.")
    
    trainEvaluatePlot(30,train_dataloader,validate_dataloader,2,resNet18=resNet18)

    test_dataloader = dataLoaders[3][2]
    predictions, trueLabels = Test(test_dataloader,resNet18)
    predictions = predictions.argmax(dim=1)
    drawConfusionMatrix(trueLabels,predictions,test_dataset.dataset.classes)
      

def trainEvaluatePlot(epochNum,train_dataloader,validate_dataloader,modelNum,lr=0.001,batchSize=300,resNet18 =""):
    print("Model:",modelNum,"Batch Size:",batchSize, "Learning Rate:",lr)
    if (modelNum == 0):
        model = Model1(0.35).to(device)
    if (modelNum == 1):
        model = Model2(0.35).to(device)
    if (modelNum == 2):
        model = resNet18
        
    optimizer = torch.optim.Adam(model.parameters(), lr=lr)
    criterion = nn.CrossEntropyLoss()

    train_loss = []
    validate_loss = []
    train_accuracy = []
    validate_accuracy = []

    for epoch in range(epochNum):
        train_loss_epoch, train_accuracy_epoch = Train(train_dataloader,model,criterion,optimizer)
        validate_loss_epoch, validate_accuracy_epoch = Validate(validate_dataloader,model,criterion)
        train_loss.append(train_loss_epoch)
        train_accuracy.append(train_accuracy_epoch)
        validate_loss.append(validate_loss_epoch)
        validate_accuracy.append(validate_accuracy_epoch)

    plt.figure(figsize=(8, 5))
    plt.plot(range(1, len(train_loss)+1), train_loss, 'g', label='Training Loss')
    plt.plot(range(1, len(validate_loss)+1), validate_loss, 'b', label='Validating Loss')

    plt.title('Training and Validating loss')
    plt.xlabel('Epochs')
    plt.ylabel('Loss')
    plt.legend()
    plt.show()

    plt.figure(figsize=(8, 5))
    plt.plot(range(1, len(train_accuracy)+1), train_accuracy, 'g', label='Training Accuracy')
    plt.plot(range(1, len(validate_accuracy)+1), validate_accuracy, 'b', label='Validate Accuracy')

    plt.title('Training and Validating Accuracy')
    plt.xlabel('Epochs')
    plt.ylabel('Accuracy')
    plt.legend()
    plt.show()

    return model

class Model1(nn.Module):
    def __init__(self,dropout_value):
        super(Model1, self).__init__()
        
        self.model = nn.Sequential(
            nn.Conv2d(in_channels=3, out_channels=16, 
                      kernel_size=7, stride=3, padding=2),
            nn.BatchNorm2d(16),
            nn.ReLU(),
            nn.Dropout(dropout_value),
            nn.Conv2d(in_channels=16, out_channels=32, 
                      kernel_size=7, stride=2, padding=1),
            nn.BatchNorm2d(32),
            nn.ReLU(),
            nn.Dropout(dropout_value),
            nn.Conv2d(in_channels=32, out_channels=64, 
                      kernel_size=5, stride=1, padding=1),
            nn.BatchNorm2d(64),
            nn.ReLU(),
            nn.Dropout(dropout_value),
            nn.Conv2d(in_channels=64, out_channels=64, 
                      kernel_size=3, stride=1, padding=0), 
            nn.BatchNorm2d(64),
            nn.ReLU(),
            nn.Dropout(dropout_value),
            nn.Conv2d(in_channels=64, out_channels=96, 
                      kernel_size=3, stride=2, padding=0),            
            nn.BatchNorm2d(96),
            nn.ReLU(),
            nn.Dropout(dropout_value),
            nn.MaxPool2d(kernel_size=3,stride=2),

        ).to(device)
        
        self.classifier = nn.Sequential(
            nn.Flatten(),
            nn.Linear(2400, 345),
            nn.ReLU(),
            nn.Dropout(dropout_value),
            nn.Linear(345, 16)
        ).to(device)
        
    def forward(self, x):
        x = self.model(x)
        x = self.classifier(x)
        return x

class Model2(nn.Module):
    dropout_value = 0
    def __init__(self,dropout_value):
        super(Model2, self).__init__()
        
        self.conv1 = nn.Sequential(
            nn.Conv2d(in_channels=3, out_channels=8, kernel_size=7, stride=3, padding=2),
            nn.BatchNorm2d(8),
        ).to(device)
        self.conv2 = nn.Sequential(
            nn.Conv2d(in_channels=8, out_channels=16, kernel_size=3, stride=1, padding=1), 
            nn.BatchNorm2d(16),
        ).to(device)
        self.conv3 = nn.Sequential(
            nn.Conv2d(in_channels=16, out_channels=8, kernel_size=3, stride=1, padding=1), 
            nn.BatchNorm2d(8),
        ).to(device)
        self.conv4 = nn.Sequential(
            nn.Conv2d(in_channels=8, out_channels=64, kernel_size=3, stride=1, padding=1), 
            nn.BatchNorm2d(64),
        ).to(device)
        self.conv5 = nn.Sequential(
            nn.Conv2d(in_channels=64, out_channels=8, kernel_size=3, stride=1, padding=1), 
            nn.BatchNorm2d(8),
        ).to(device)
        self.relu = nn.Sequential(
            nn.ReLU(),
        )
        self.maxPool = nn.Sequential(
            nn.MaxPool2d(kernel_size=3,stride=2),
        )
        
        self.classifier = nn.Sequential(
            nn.Flatten(),
            nn.Linear(1568, 250),
            nn.ReLU(),
            nn.Dropout(dropout_value),
            nn.Linear(250, 16)
        ).to(device)
        
    def forward(self, x):
        x = self.conv1(x)
        x = self.relu(x)
        
        residual = x.clone()     # Beginning of residual connection 1
        
        x = self.conv2(x)
        x = self.relu(x)
        x = self.conv3(x)
        
        x += residual            # End of residual connection 1
        
        x = self.relu(x)
        x = self.maxPool(x)
        
        residual2 = x.clone()    # Beginning of residual connection 2
        
        x = self.conv4(x)
        x = self.relu(x)
        x = self.conv5(x)
        
        x += residual2           # End of residual connection 2
        
        x = self.relu(x)
        x = self.maxPool(x)
        x = self.classifier(x)
        return x
    
def Train(train_dataloader,model,criterion,optimizer):
    total_loss = 0
    start_time = time()
    
    accuracy = []
    
    for i, data in enumerate(train_dataloader, 0):
        inputs, label = data
        
        output = model(inputs)
        
        loss = criterion(output, label) 
        total_loss += loss.item()
        
        optimizer.zero_grad()
        loss.backward()
        optimizer.step()
        
        argmax = output.argmax(dim=1) 
        accuracy.append((label==argmax).sum().item() / label.shape[0])
    
    return total_loss / len(train_dataloader), sum(accuracy)/len(accuracy)


def Validate(validate_dataloader,model,criterion):
    total_loss = 0
    start_time = time()

    accuracy = []
    
    with torch.no_grad(): 
        for i, data in enumerate(validate_dataloader):
            inputs, label = data
            
            output = model(inputs)

            loss = criterion(output, label)
            total_loss += loss.item()
            
            argmax = output.argmax(dim=1) 
            accuracy.append((label==argmax).sum().item() / label.shape[0])
    return total_loss/len(validate_dataloader), sum(accuracy)/len(accuracy)

def Test(test_dataloader,model,criterion=nn.CrossEntropyLoss()):
    total_loss = 0
    start_time = time()
    accuracy = []
    predictions = torch.tensor([])
    
    with torch.no_grad(): 
        for i, data in enumerate(test_dataloader):
            inputs, label = data
            output = model(inputs)
            predictions = torch.cat((predictions,output),dim=0)

            loss = criterion(output, label)
            total_loss += loss.item()
            
            argmax = output.argmax(dim=1) 
            accuracy.append((label==argmax).sum().item() / label.shape[0])
    
    print('Test Loss: {:.4f}, Accuracy: {:.2f}'.format(
        total_loss/len(test_dataloader), sum(accuracy)/len(accuracy)
    ))

    return predictions

def drawConfusionMatrix(testLabels,predictions,categories):
    confMatrix = confusion_matrix(testLabels,predictions)
    np.set_printoptions(precision=2)
    confMatrixNormalized = confMatrix.astype('float') / confMatrix.sum(axis=1)[:, np.newaxis]
    plt.figure()
    plt.imshow(confMatrix, interpolation='nearest', cmap=plt.cm.Blues)
    plt.colorbar()
    tick_marks = np.arange(len(categories))
    plt.xticks(tick_marks, categories, rotation=45)
    plt.yticks(tick_marks, categories)
    plt.tight_layout()
    plt.ylabel('True label')
    plt.xlabel('Predicted label')
    plt.show()

main()