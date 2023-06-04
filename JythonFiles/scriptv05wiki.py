"""
Typen für einen Aufruf von createBotWithType und createBotWithRouteAndType:
sport, pickup, suv, sedan, van, bike, jeep, limo, mini, muscle

Routen-Beschreibung für createBotWithRoute und createBotWithRouteAndType:
String mit l (left) oder r (right) oder s (straight) mit Kommas getrennt. z.B. "l,r,l,s"

"""
botAPI.createBot()
botAPI.createBotWithType("limo")
for i in range(0,3):    
    botAPI.createBotWithType("bike")
botAPI.createBotWithRoute("r,l,l")
botAPI.createBotWithRouteAndType("l,l,r,s,r", "van")


botAPI.start()