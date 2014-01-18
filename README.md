balancebot
==========

This bot will get your Mt.Gox acount balance and BTC price every X minutes and store them in CSV file. The CSV file can be used to make graph with Excel to compare how well you have traded VS how much bitcoin has risen if you convert them to percents. Example chart with 60 min interval used: http://oi44.tinypic.com/2wf01so.jpg

How to use:
1. Download the bot from https://github.com/jussirantala/balancebot/blob/master/balancebot_release.zip?raw=true
2. Extract files
3. Open goxbot.cfg with a text editor
4. Log in to mt.gox website and create new API key for this bot
5. Enter API key details to the config file you opened
6. Change interval to how many minutes you want the bot to wait between getting and saving data
7. Open command line or terminal depending on your operating system and navigate to the folder of extracted files
8. Start the bot with this command: java -jar balancebot.jar
9. Let it run 24/7 (I run it at my Linux server). If you are using linux, you can make it to run on background by starting it like this: screen java -jar balancebot.jar

10. Donate if you like it: 
    BTC:   158UQ2niJ6eVQgPsfJaS791s9hBruVWBUU
    LTC:   LauBymAt8Kkz9CLmZ8QA5h8rfV6DACfVSS
    Doge:  DQsANjjfnbBDR42S9NrWfcswRXiFDC9xr1
    PPC:   PF7y9TCDwBGcZkaRGTFPpN3SNM7q69i3Kc
