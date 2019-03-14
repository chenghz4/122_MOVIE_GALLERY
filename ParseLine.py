import re

try:
    f = open("test.log", "r")
    x = f.read()
    p = x.split('\n')
    #print p
    jdbc = 0
    doget = 0
    countjdbc = 0
    countdoget = 0
    for c in p:
        x = c
        arr = re.split('[ :]', x)
        for t in arr:
            if t == 'JDBC':
                jdbc = (int)(jdbc) + (int)(arr[arr.index(t) + 1])
                countjdbc = countjdbc + 1
            if t == 'doget':
                doget = (int)(doget) + (int)(arr[arr.index(t) + 1])
                countdoget = countdoget + 1

        jdbc = (int)(jdbc)
        doget = (int)(doget)

    print('Average jdbc is ', (jdbc / countjdbc))
    print('Average doget is ', (doget / countdoget))

finally:
    f.close()