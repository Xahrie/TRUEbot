package de.zahrie.trues.api.discord.builder.modal;

import de.zahrie.trues.api.discord.util.Replyer;
import lombok.EqualsAndHashCode;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.interactions.modals.Modal;

@EqualsAndHashCode(callSuper = true)
public abstract class ModalBase extends Replyer {
  protected Modal.Builder builder;

  public ModalBase() {
    super(ModalInteractionEvent.class);
    final View annotation = getClass().asSubclass(this.getClass()).getAnnotation(View.class);
    if (annotation == null) {
      return;
    }
    this.name = annotation.value();
  }

  public abstract Modal getModal(boolean value);

  public abstract boolean execute(ModalInteractionEvent event);
}
