package lu.forex.system.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Collection;
import lombok.AccessLevel;
import lombok.Getter;
import lu.forex.system.dtos.SymbolCreateDto;
import lu.forex.system.dtos.SymbolResponseDto;
import lu.forex.system.dtos.SymbolUpdateDto;
import lu.forex.system.exceptions.SymbolNotFoundException;
import lu.forex.system.services.SymbolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/symbols")
@Getter(AccessLevel.PRIVATE)
public class SymbolController {

  private final SymbolService symbolService;

  @Autowired
  public SymbolController(final SymbolService symbolService) {
    this.symbolService = symbolService;
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Collection<SymbolResponseDto> getSymbols() {
    return this.getSymbolService().getSymbols();
  }

  @GetMapping("/{name}")
  @ResponseStatus(HttpStatus.OK)
  public SymbolResponseDto getSymbol(@PathVariable @NotNull @NotBlank @Size(max = 6, min = 6) String name) {
    return this.getSymbolService().getSymbol(name).orElseThrow(SymbolNotFoundException::new);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public SymbolResponseDto addSymbol(@RequestBody @NotNull @Valid SymbolCreateDto symbolCreateDto) {
    return this.getSymbolService().addSymbol(symbolCreateDto);
  }

  @PutMapping("/{name}")
  @ResponseStatus(HttpStatus.CREATED)
  public SymbolResponseDto updateSymbol(@RequestBody @NotNull @Valid SymbolUpdateDto symbolUpdateDto,
      @PathVariable @NotNull @NotBlank @Size(max = 6, min = 6) String name) {
    return this.getSymbolService().updateSymbol(symbolUpdateDto, name);
  }

  @DeleteMapping("/{name}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteSymbol(@PathVariable @NotNull @NotBlank @Size(max = 6, min = 6) String name) {
    if (!this.getSymbolService().deleteSymbol(name)) {
      throw new SymbolNotFoundException();
    }
  }
}
