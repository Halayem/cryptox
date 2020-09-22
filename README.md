# Cryptox for Crypto Exchange

#####Kraken service examples

    public void buyAddOrderWithStopLossProfit() {
        exchangeService.addOrder(
                AddOrderInput
                        .builder   ()
                        .assetPair (AssetPair
                                .builder  ()
                                .from     (XzAsset.XLTC)
                                .to       (XzAsset.ZEUR)
                                .build    ()
                        )
                        .addOrderType   (AddOrderType.BUY)
                        .orderType      (OrderType.LIMIT)
                        .applicationAssetPairMarketTicker          (new BigDecimal("39.38"  ))
                        .volume         (new BigDecimal(1   ))
                        .leverage       ("2:1")
                        .close          (AddOrderInput.Close.builder()
                                                            .orderType(OrderType.STOP_LOSS_PROFIT_LIMIT)
                                                            .stopLossPriceRelativePercentageDelta   ( 5  )
                                                            .takeProfitPriceRelativeDelta           ( 1 )
                                                            .build())
                        .build()
        ).subscribe(addOrderOutput -> log.info( "order placed, kraken response: {}", addOrderOutput ) );
    }
    
    public void buyAddOrderWithMarketOrderType() {
            exchangeService.addOrder(
                    AddOrderInput.builder        ()
                                 .assetPair      (AssetPair.builder  ()
                                                           .from     (XzAsset.XLTC)
                                                           .to       (XzAsset.ZEUR)
                                                           .build    ()
                                 )
                                 .addOrderType   (AddOrderType.BUY)
                                 .orderType      (OrderType.MARKET)
                                 .volume         (new BigDecimal("0.1"   ))
                                 .build          ()
            ).subscribe(addOrderOutput -> log.info( "order placed, kraken response: {}", addOrderOutput ));
        }
    
        public void sellAddOrder() {
            exchangeService.addOrder(
                AddOrderInput.builder   ()
                        .assetPair      (AssetPair.builder  ()
                                                  .from     (XzAsset.XLTC)
                                                  .to       (XzAsset.ZEUR)
                                                  .build    ()
                                        )
                        .addOrderType   (AddOrderType.SELL)
                        .orderType      (OrderType.LIMIT)
                        .applicationAssetPairMarketTicker          (new BigDecimal(40  ))
                        .volume         (new BigDecimal(1   ))
                        .build()
            ).subscribe(addOrderOutput -> log.info( "order placed, kraken response: {}", addOrderOutput ));
        }
    
        public void getBalance() {
            exchangeService.getBalance().subscribe(response -> {
                if ( response != null && response.getResult() != null ) {
                    log.info("balance in litecoin: {}", response.getResult().get( XzAsset.XLTC  ));
                    log.info("balance in euro: {}",     response.getResult().get( XzAsset.ZEUR ));
                }
            });
        }
    
        public void getTradeBalance() {
            exchangeService.getTradeBalance(AssetClass.CURRENCY).subscribe(response -> log.info("trade balance: {}", response));
        }
    
        public void getOpenOrders() {
            exchangeService.getOpenOrders().subscribe(response -> log.info("open orders: {}", response));
        }
