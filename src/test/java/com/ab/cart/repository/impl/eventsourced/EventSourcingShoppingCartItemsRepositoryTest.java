package com.ab.cart.repository.impl.eventsourced;

import com.ab.cart.domain.CartItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EventSourcingShoppingCartItemsRepositoryTest {

    @Mock
    AggregatingShoppingCartFactory aggregatingShoppingCartFactory;
    @Mock
    ShoppingCartEventSource shoppingCartEventSource;

    @Test
    public void shouldUseDependencies() {
        AggregatingShoppingCart aggregatingShoppingCart = mock(AggregatingShoppingCart.class);
        when(aggregatingShoppingCartFactory.create()).thenReturn(aggregatingShoppingCart);
        List<CartItem> resultList = new ArrayList<>();
        when(aggregatingShoppingCart.getItems()).thenReturn(resultList);
        EventSourcingShoppingCartItemsRepository repository = new EventSourcingShoppingCartItemsRepository(aggregatingShoppingCartFactory,
                                                                                    shoppingCartEventSource);
        assertThat(repository.getShoppingCartItems(), is(sameInstance(resultList)));
        verify(shoppingCartEventSource).readInto(aggregatingShoppingCart);

    }
}
