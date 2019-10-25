package com.ugurdonmez.wallet.domain.player;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ugurdonmez.wallet.domain.player.exceptions.PlayerBalanceIsLessThanZeroException;
import com.ugurdonmez.wallet.domain.player.exceptions.PlayerNotFoundException;
import com.ugurdonmez.wallet.domain.transaction.Transaction;
import com.ugurdonmez.wallet.domain.transaction.TransactionType;
import com.ugurdonmez.wallet.domain.transaction.exception.TransactionAmountLessThanZeroException;
import com.ugurdonmez.wallet.domain.transaction.exception.TransactionIdIsNotUniqueException;
import com.ugurdonmez.wallet.service.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlayerController.class)
class PlayerControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private WalletService walletService;

    @Test
    void testOne() throws Exception {
        UUID id = UUID.randomUUID();
        when( walletService.getPlayerBalance( id ) )
                .thenReturn( BigDecimal.TEN );

        mvc
                .perform( get( String.format( "/wallet/player/%s", id.toString() ) ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() )
                .andExpect( content().string( "10" ) );
    }

    @Test
    void getTransaction() throws Exception {
        Player player = new Player( UUID.randomUUID(), BigDecimal.TEN, new LinkedList<>() );

        when( walletService.getTransactions( player.getId() ) )
                .thenReturn( player.getTransactions() );

        mvc
                .perform( get( String.format( "/wallet/player/%s/transactions", player.getId().toString() ) ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() )
                .andExpect( content().string( "[]" ) );


        Transaction tx = new Transaction( UUID.randomUUID(), BigDecimal.ONE, TransactionType.DEBIT );
        player.getTransactions().add( tx );

        mvc
                .perform( get( String.format( "/wallet/player/%s/transactions", player.getId().toString() ) ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() )
                .andExpect( content().string( String.format( "[{\"id\":\"%s\",\"amount\":1,\"type\":\"DEBIT\"}]", tx.getId() ) ) );
    }

    @Test
    void postTransaction() throws Exception {
        Transaction tx = new Transaction( UUID.randomUUID(), BigDecimal.ONE, TransactionType.DEBIT );
        Player player = new Player( UUID.randomUUID(), BigDecimal.TEN, new LinkedList<>( Arrays.asList( tx ) ) );

        when( walletService.transaction( player.getId(), tx ) )
                .thenReturn( player );

        mvc
                .perform( post( String.format( "/wallet/player/%s", player.getId().toString() ) )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( asJsonString( tx ) ) )
                .andExpect( status().isOk() );
    }

    @Test
    void testPlayerNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        Transaction tx = new Transaction( UUID.randomUUID(), BigDecimal.ONE, TransactionType.DEBIT );

        when( walletService.getPlayerBalance( id ) )
                .thenThrow( PlayerNotFoundException.class );

        when( walletService.transaction( id, tx ) )
                .thenThrow( PlayerNotFoundException.class );

        when( walletService.getTransactions( id ) )
                .thenThrow( PlayerNotFoundException.class );

        mvc
                .perform( get( String.format( "/wallet/player/%s", id.toString() ) ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isNotFound() );

        mvc
                .perform( post( String.format( "/wallet/player/%s", id.toString() ) )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( asJsonString( tx ) ) )
                .andExpect( status().isNotFound() );

        mvc
                .perform( get( String.format( "/wallet/player/%s/transactions", id.toString() ) ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isNotFound() );
    }

    @Test
    void testTransactionAmountLessThanZero() throws Exception {
        UUID id = UUID.randomUUID();
        Transaction tx = new Transaction( UUID.randomUUID(), BigDecimal.valueOf( -1 ), TransactionType.DEBIT );

        when( walletService.transaction( id, tx ) )
                .thenThrow( TransactionAmountLessThanZeroException.class );

        mvc
                .perform( post( String.format( "/wallet/player/%s", id.toString() ) )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( asJsonString( tx ) ) )
                .andExpect( status().isExpectationFailed() );
    }

    @Test
    void testTransactionIsNotUnique() throws Exception {
        UUID id = UUID.randomUUID();
        Transaction tx = new Transaction( UUID.randomUUID(), BigDecimal.valueOf( -1 ), TransactionType.DEBIT );

        when( walletService.transaction( id, tx ) )
                .thenThrow( TransactionIdIsNotUniqueException.class );

        mvc
                .perform( post( String.format( "/wallet/player/%s", id.toString() ) )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( asJsonString( tx ) ) )
                .andExpect( status().isConflict() );
    }

    @Test
    void testPlayerBalanceIsLessThanZero() throws Exception {
        UUID id = UUID.randomUUID();
        Transaction tx = new Transaction( UUID.randomUUID(), BigDecimal.valueOf( -1 ), TransactionType.DEBIT );

        when( walletService.transaction( id, tx ) )
                .thenThrow( PlayerBalanceIsLessThanZeroException.class );

        mvc
                .perform( post( String.format( "/wallet/player/%s", id.toString() ) )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( asJsonString( tx ) ) )
                .andExpect( status().isExpectationFailed() );
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString( obj );
        } catch (Exception e) {
            throw new RuntimeException( e );
        }
    }
}