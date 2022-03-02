package fr.stonksdev.backend.controllers;

import fr.stonksdev.backend.controllers.dto.ErrorDTO;
import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.exceptions.ImpossibleCreationException;
import fr.stonksdev.backend.exceptions.ItemNotFoundException;
import fr.stonksdev.backend.exceptions.WrongInputException;
import fr.stonksdev.backend.interfaces.EventModifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.events.Event;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
//@RequestMapping(path = EventController.BASE_URI, produces = APPLICATION_JSON_VALUE)
// referencing the same BASE_URI as Customer care to extend it hierarchically
public class EventController {
    //public static final String EVENT_URI = "/{customerId}/cart";

    @Autowired
    private EventModifier event;

    @ExceptionHandler({ImpossibleCreationException.class})
    public ResponseEntity<ErrorDTO> handleExceptions(ImpossibleCreationException e)  {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("Object creation not possible");
        errorDTO.setDetails(e.getId() + " is not a valid object Id");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
    }

    @ExceptionHandler({WrongInputException.class})
    public ResponseEntity<ErrorDTO> handleExceptions(WrongInputException e)  {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("Wrong parameter");
        errorDTO.setDetails("The parameter " + e.getName() + "");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDTO);
    }

    @ExceptionHandler({ItemNotFoundException.class})
    public ResponseEntity<ErrorDTO> handleExceptions(ItemNotFoundException e)  {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("Wrong parameter");
        errorDTO.setDetails("The parameter " + e.getName() + "");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDTO);
    }

    //@PostMapping(path = CART_URI, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> updateEvent(@PathVariable("eventId") String eventId, @RequestBody Activity act) throws ItemNotFoundException {
        boolean res = event.modify(act);
        return ResponseEntity.ok(res);
    }
    /*
    @GetMapping(CART_URI)
    public ResponseEntity<Set<Item>> getCustomerCartContents(@PathVariable("customerId") String customerId) throws CustomerIdNotFoundException {
        return ResponseEntity.ok(processor.contents(retrieveCustomer(customerId)));
    }

    @PostMapping(path = CART_URI+"/validate")
    public ResponseEntity<String> validate(@PathVariable("customerId") String customerId) throws CustomerIdNotFoundException, EmptyCartException, PaymentException {
        Order order = processor.validate(retrieveCustomer(customerId));
        return ResponseEntity.ok().body("Order " + order.getId() + " (amount " + order.getPrice() +
                ") is validated");
    }*/
}
