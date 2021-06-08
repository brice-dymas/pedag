package com.urservices.service.dto;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Projet:  pedag
 * Cree par: Brice dymas
 * Date Creation: 2021, Tuesday 08 of June
 */
public class NewRequeteDTO {

    private Long id;

    @NotNull
    @Size(min = 4, max = 70)
    @Column(name = "objet", length = 70, nullable = false)
    private String objet;

    @NotNull
    @Size(min = 2)
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    private Long userId;

    public NewRequeteDTO(
        Long id,
        @NotNull @Size(min = 4, max = 70) String objet,
        @NotNull @Size(min = 2) String description,
        @NotNull Long userId
    ) {
        this.id = id;
        this.objet = objet;
        this.description = description;
        this.userId = userId;
    }

    public NewRequeteDTO() {}

    public static NewRequeteDTOBuilder builder() {
        return new NewRequeteDTOBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public @NotNull @Size(min = 4, max = 70) String getObjet() {
        return this.objet;
    }

    public @NotNull @Size(min = 2) String getDescription() {
        return this.description;
    }

    public @NotNull Long getUserId() {
        return this.userId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setObjet(@NotNull @Size(min = 4, max = 70) String objet) {
        this.objet = objet;
    }

    public void setDescription(@NotNull @Size(min = 2) String description) {
        this.description = description;
    }

    public void setUserId(@NotNull Long userId) {
        this.userId = userId;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof NewRequeteDTO)) return false;
        final NewRequeteDTO other = (NewRequeteDTO) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$objet = this.getObjet();
        final Object other$objet = other.getObjet();
        if (this$objet == null ? other$objet != null : !this$objet.equals(other$objet)) return false;
        final Object this$description = this.getDescription();
        final Object other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description)) return false;
        final Object this$userId = this.getUserId();
        final Object other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !this$userId.equals(other$userId)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof NewRequeteDTO;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $objet = this.getObjet();
        result = result * PRIME + ($objet == null ? 43 : $objet.hashCode());
        final Object $description = this.getDescription();
        result = result * PRIME + ($description == null ? 43 : $description.hashCode());
        final Object $userId = this.getUserId();
        result = result * PRIME + ($userId == null ? 43 : $userId.hashCode());
        return result;
    }

    public String toString() {
        return (
            "NewRequeteDTO(id=" +
            this.getId() +
            ", objet=" +
            this.getObjet() +
            ", description=" +
            this.getDescription() +
            ", userId=" +
            this.getUserId() +
            ")"
        );
    }

    public static class NewRequeteDTOBuilder {

        private Long id;

        @NotNull
        @Size(min = 4, max = 70)
        private String objet;

        @NotNull
        @Size(min = 2)
        private String description;

        @NotNull
        private Long userId;

        NewRequeteDTOBuilder() {}

        public NewRequeteDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public NewRequeteDTOBuilder objet(@NotNull @Size(min = 4, max = 70) String objet) {
            this.objet = objet;
            return this;
        }

        public NewRequeteDTOBuilder description(@NotNull @Size(min = 2) String description) {
            this.description = description;
            return this;
        }

        public NewRequeteDTOBuilder userId(@NotNull Long userId) {
            this.userId = userId;
            return this;
        }

        public NewRequeteDTO build() {
            return new NewRequeteDTO(id, objet, description, userId);
        }

        public String toString() {
            return (
                "NewRequeteDTO.NewRequeteDTOBuilder(id=" +
                this.id +
                ", objet=" +
                this.objet +
                ", description=" +
                this.description +
                ", userId=" +
                this.userId +
                ")"
            );
        }
    }
}
