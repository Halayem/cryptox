# Cryptox - Crypto Trading Exchange (Experimental !)

Trading program, written with java 11 and reactive programming, configured and tested 
 - to trade on Kraken market place
 - litecoin/Euro as asset pair
 
If you want to play with it, you need to:
  - generate an api key and api private key on kraken.com
  - edit application-kraken.properties file with your keys
    
        application.repository.webservice.api-key=CHANGE_ME
        application.repository.webservice.private-key=CHANGE_ME
        
This program let you trade automatically with a simple strategy that I've called bearing strategy, that means if a cryptocurrency 
price reach a configured gap, so it will place buy or sell placement request with current market ask price
Consider this configuration example:

        application.currency.tradings.litecoin-euro.enabled=true
        application.currency.tradings.litecoin-euro.bearing-strategy.gap=0.5
        application.currency.tradings.litecoin-euro.bearing-strategy.amount-to-sell=0.2
        application.currency.tradings.litecoin-euro.bearing-strategy.amount-to-buy=0.3
        
Initial state: litecoin price is €40
 - if price hit €40,5 so a sell placement of amount litecoin 0,2 will be placed
 - if price hit €39,5 a buy placement of amount 0,3 will be placed
 - else the program will do nothing
 
When successive gaps are reached, the amount to sell or to buy will be multiplied incrementally  

Initial state: litecoin price is €40
 - if price hit €40,5 so a sell placement of amount litecoin 0,2 will be placed
 - if price hit €41 a sell placement of amount litecoin 2 X 0,2  = 0,4 will be placed
 - if price hit €41,5 a sell placement of amount litecoin 3 X 0,2  = 0,6 will be placed, and so on ...
 - At the first low gap, the amount multiplier will be reset to 1 !
 



