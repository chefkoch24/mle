from random import randint
from random import random
from _operator import index

class QLearningAgent:
    
    #variables for all Objects of QLearningAgent
    alpha = 0.1 #learnrate
    epsilon = 0.1 #10% for Epsilon Greedy
    gamma = 0.9 #Discount factor
    #Constructor
    def __init__(self):
        #initalize q_table
        self.q_table = []
        #field is 9 units long
        for i in range(9):
            obj = [];
            #Three actions: left, stay, right
            for j in range(3):
                obj.append(randint(0,1))
                self.q_table.append(obj)
        print("Initalize q_table:", self.q_table)
    
    def indexOfMaxElement(self, l):
        index = 0
        max = 0
        for i in range(len(l)):
            if l[i] > max:
                index = i
        return index

    def learn(self, s, a, r, s_next):
        q_table_old= self.q_table[s][a]
        next_max_action = self.chooseAction(s_next)
        q_table_new = q_table_old + QLearningAgent.alpha * (r + QLearningAgent.gamma * max(self.q_table[s_next]) - q_table_old)
        self.q_table[s][a] = q_table_new
        print("new q", self.q_table)
    
        s = s_next
    
    def chooseAction(self, s):
        a = 0 #action
        #choose a from s using epsilon-greedy
        #epsilon greedy
        e = random()
        print("e", e)
        if(e < QLearningAgent.epsilon):
            #choose random
            temp = randint(-1,1)
            #return index of action
            a = temp
        else:
            a = self.indexOfMaxElement(self.q_table[s])
            print(a)     
        return a     
            