from random import randint
from random import random
from _operator import index

class QLearningAgent:

    #variables for all Objects of QLearningAgent
    alpha = 0.1 #learnrate
    epsilon = 0.1 #10% for Epsilon Greedy
    gamma = 0.8 #Discount factor
    #Constructor
    def __init__(self, xBallMax, yBallMax, schlaegerMax, xVmax, yVmax):
        #initalize q_table
        self.q_table = []
        s = self.calculate_state(schlaegerMax, schlaegerMax, xBallMax, yBallMax, yBallMax, xVmax, yVmax)
        #field is 9 units long
        for i in range(s):
            obj = [];
            #Three actions: left, stay, right
            for j in range(3):
                obj.append(float(randint(0,1)))
                self.q_table.append(obj)
        #print("Initalize q_table:", self.q_table)

    def indexOfMaxElement(self, l):
        index = 0
        max = 0.0
        for i in range(len(l)):
            if l[i] > max:
                index = i
                max  = l[i]
        return index

    def learn(self, s, s_next, a, r):
        print("------------------------")
        print("learn")
        print("s: ", s)
        print("s_next: ", s_next)
        print("action", a)
        print("reward", r)
        q_table_old = self.q_table[s][a]
        print("q-old", q_table_old)
        print("actions at s " ,self.q_table[s])
        q_table_new = q_table_old + (QLearningAgent.alpha * (r + (QLearningAgent.gamma * max(self.q_table[s_next]) - q_table_old)))
        print("q-new", q_table_new)
        self.q_table[s][a] = q_table_new
        print("actions at s ",self.q_table[s])
        print("actions at s_next ",self.q_table[s_next])

    def choose_action(self, s):
        a = 0 #action
        #choose a from s using epsilon-greedy
        #epsilon greedy
        e = random()
        if(e < QLearningAgent.epsilon):
            #choose random
            temp = randint(0,2)
            print("e-greedy")
            #return index of action
            a = temp
        else:
            print("max-action")
            print(self.q_table[s])
            a = self.indexOfMaxElement(self.q_table[s])
        return a

    def calculate_state(self, xSchlaeger, schlaegerMax, xBall, yBall, yMax, xV, yV):
        xV = (xV + 1)/2;
        yV = (yV + 1)/2;
        # 1 is vMax
        return int((((((xBall * yMax) + yBall)* schlaegerMax + xSchlaeger) * 1 + xV) * 1 + yV))