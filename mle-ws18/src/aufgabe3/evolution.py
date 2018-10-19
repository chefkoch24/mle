from random import randint
from random import random
 
P = 100
LENGTH = 100
R = 0.05
M = 0.01
#Target
finalBitString = [];
for i in range(LENGTH):
    finalBitString.append(randint(0,1))
print("Target:" ,finalBitString)

#Individuen
population = [];
for i in range(P):
    population.append([])
    for j in range(LENGTH):
        population[i].append(randint(0,1))

#Fitness
fitnesses = [];

def getFitness(ar):
    fitness = 0
    for i in range(P):
        if(ar[i] == finalBitString[i]):
            fitness += 1 
    return fitness

for i in range(P):
    fitnesses.append(getFitness(population[i]))

def probability(index):
    return fitnesses[index]/sum(fitnesses)

def selectHyphotesis():
    summe= 0.0
    i = randint(0,P)
    randNum = random()
    while (summe < randNum):
        i += 1
        i = i % P 
        summe = summe + probability(i)
    return i
    
#algorithm
maxFitness = max(fitnesses)

while (maxFitness<LENGTH):
    nextGeneration = [];
    #Select the best
    nextGeneration.append(population[fitnesses.index(max(fitnesses))].copy())
    #Selection
    i = 0
    while (i < ((1-R)*P)-1):
        nextGeneration.append(population[selectHyphotesis()].copy())
        i += 1
    
    i = 0
    while (i < (R*P)/2):
        mum = population[selectHyphotesis()]
        dad = population[selectHyphotesis()]
        child1 = []
        child2 = []
        j = 0
        while (j < LENGTH):
            if(j < LENGTH/2):
                child1.append(mum[j])
                child2.append(dad[j])
            else:
                child1.append(dad[j])
                child2.append(mum[j])
            j += 1    
        i += 1 
        nextGeneration.append(child1)
        nextGeneration.append(child2)
    #mutation
    i = 0
    while(i < P*M):
        i += 1
        mIndex1 = randint(0,P)
        mIndex2 = randint(0,LENGTH)
        if(nextGeneration[mIndex1 % P][mIndex2 % LENGTH] == 0):
            nextGeneration[mIndex1 % P][mIndex2 % LENGTH] = 1
        else:
            nextGeneration[mIndex1 % P][mIndex2 % LENGTH] = 0
    for x in range(0,P):
        fitnesses[x] = getFitness(nextGeneration[x])  
    population = nextGeneration
    maxFitness = max(fitnesses)
    print("Fitness: ", maxFitness)
print("Fertig")
print("Target: ", finalBitString)
print("Population: ", population)
