package com.mycelium.wallet.lt.api;

import java.util.Collection;
import java.util.UUID;

import com.mycelium.lt.api.LtApi;
import com.mycelium.lt.api.LtApiException;
import com.mycelium.lt.api.model.Ad;
import com.mycelium.wallet.lt.LocalTraderEventSubscriber;
import com.mycelium.wallet.lt.LocalTraderManager.LocalManagerApiContext;

public class GetAd extends Request {
   private static final long serialVersionUID = 1L;

   private UUID _adId;

   public GetAd(UUID adId) {
      super(true, true);
      _adId = adId;
   }

   @Override
   public void execute(LocalManagerApiContext context, LtApi api, UUID sessionId,
         Collection<LocalTraderEventSubscriber> subscribers) {

      try {
         // Call function
         final Ad result = api.getAd(sessionId, _adId).getResult();

         // Notify
         synchronized (subscribers) {
            for (final LocalTraderEventSubscriber s : subscribers) {
               s.getHandler().post(new Runnable() {

                  @Override
                  public void run() {
                     s.onLtAdRetrieved(result, GetAd.this);
                  }
               });
            }
         }

      } catch (LtApiException e) {
         // Handle errors
         context.handleErrors(this, e.errorCode);
      }

   }
}