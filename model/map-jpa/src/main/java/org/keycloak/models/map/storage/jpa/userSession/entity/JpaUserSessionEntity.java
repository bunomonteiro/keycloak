/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.keycloak.models.map.storage.jpa.userSession.entity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.keycloak.models.UserSessionModel;
import org.keycloak.models.map.common.DeepCloner;
import org.keycloak.models.map.common.UuidValidator;
import org.keycloak.models.map.storage.jpa.Constants;
import org.keycloak.models.map.storage.jpa.JpaRootVersionedEntity;
import org.keycloak.models.map.storage.jpa.hibernate.jsonb.JsonbType;
import org.keycloak.models.map.userSession.MapAuthenticatedClientSessionEntity;
import org.keycloak.models.map.userSession.MapUserSessionEntity.AbstractUserSessionEntity;

import static org.keycloak.models.map.storage.jpa.JpaMapStorageProviderFactory.CLONER;

/**
 * There are some fields marked by {@code @Column(insertable = false, updatable = false)}.
 * Those fields are automatically generated by database from json field,
 * therefore marked as non-insertable and non-updatable to instruct hibernate.
 */
@Entity
@Table(name = "kc_user_session")
@TypeDefs({@TypeDef(name = "jsonb", typeClass = JsonbType.class)})
public class JpaUserSessionEntity extends AbstractUserSessionEntity implements JpaRootVersionedEntity {

    @Id
    @Column
    private UUID id;

    //used for implicit optimistic locking
    @Version
    @Column
    private int version;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private final JpaUserSessionMetadata metadata;

    @Column(insertable = false, updatable = false)
    @Basic(fetch = FetchType.LAZY)
    private Integer entityVersion;

    @Column(insertable = false, updatable = false)
    @Basic(fetch = FetchType.LAZY)
    private String realmId;

    @Column(insertable = false, updatable = false)
    @Basic(fetch = FetchType.LAZY)
    private String userId;

    @Column(insertable = false, updatable = false)
    @Basic(fetch = FetchType.LAZY)
    private String brokerSessionId;

    @Column(insertable = false, updatable = false)
    @Basic(fetch = FetchType.LAZY)
    private String brokerUserId;

    @Column(insertable = false, updatable = false)
    @Basic(fetch = FetchType.LAZY)
    private Boolean offline;

    @Column(insertable = false, updatable = false)
    @Basic(fetch = FetchType.LAZY)
    private Long lastSessionRefresh;

    @Column(insertable = false, updatable = false)
    @Basic(fetch = FetchType.LAZY)
    private Long expiration;

    @OneToMany(mappedBy = "root", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final Set<JpaUserSessionNoteEntity> notes = new HashSet<>();

    @OneToMany(mappedBy = "root", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final Set<JpaClientSessionEntity> clientSessions = new HashSet<>();

    /**
     * No-argument constructor, used by hibernate to instantiate entities.
     */
    public JpaUserSessionEntity() {
        this.metadata = new JpaUserSessionMetadata();
    }

    public JpaUserSessionEntity(DeepCloner cloner) {
        this.metadata = new JpaUserSessionMetadata(cloner);
    }

    public boolean isMetadataInitialized() {
        return metadata != null;
    }

    @Override
    public Integer getEntityVersion() {
        if (isMetadataInitialized()) return metadata.getEntityVersion();
        return entityVersion;
    }

    @Override
    public void setEntityVersion(Integer entityVersion) {
        metadata.setEntityVersion(entityVersion);
    }

    @Override
    public Integer getCurrentSchemaVersion() {
        return Constants.CURRENT_SCHEMA_VERSION_USER_SESSION;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public String getId() {
        return id == null ? null : id.toString();
    }

    @Override
    public void setId(String id) {
        String validatedId = UuidValidator.validateAndConvert(id);
        this.id = UUID.fromString(validatedId);
    }

    @Override
    public String getRealmId() {
        if (isMetadataInitialized()) return metadata.getRealmId();
        return realmId;
    }

    @Override
    public void setRealmId(String realmId) {
        metadata.setRealmId(realmId);
    }

    @Override
    public String getUserId() {
        if (isMetadataInitialized()) return metadata.getUserId();
        return userId;
    }

    @Override
    public void setUserId(String userId) {
        metadata.setUserId(userId);
    }

    @Override
    public String getLoginUsername() {
        return metadata.getLoginUsername();
    }

    @Override
    public void setLoginUsername(String loginUsername) {
        metadata.setLoginUsername(loginUsername);
    }

    @Override
    public String getIpAddress() {
        return metadata.getIpAddress();
    }

    @Override
    public void setIpAddress(String ipAddress) {
        metadata.setIpAddress(ipAddress);
    }

    @Override
    public String getAuthMethod() {
        return metadata.getAuthMethod();
    }

    @Override
    public void setAuthMethod(String authMethod) {
        metadata.setAuthMethod(authMethod);
    }

    @Override
    public Boolean isOffline() {
        if (isMetadataInitialized()) return metadata.isOffline();
        return offline;
    }

    @Override
    public void setOffline(Boolean offline) {
        metadata.setOffline(offline);
    }

    @Override
    public Boolean isRememberMe() {
        return metadata.isRememberMe();
    }

    @Override
    public void setRememberMe(Boolean rememberMe) {
        metadata.setRememberMe(rememberMe);
    }

    @Override
    public Long getTimestamp() {
        return metadata.getTimestamp();
    }

    @Override
    public void setTimestamp(Long timestamp) {
        metadata.setTimestamp(timestamp);
    }

    @Override
    public Long getLastSessionRefresh() {
        if (isMetadataInitialized()) return metadata.getLastSessionRefresh();
        return lastSessionRefresh;
    }

    @Override
    public void setLastSessionRefresh(Long lastSessionRefresh) {
        metadata.setLastSessionRefresh(lastSessionRefresh);

    }

    @Override
    public Long getExpiration() {
        if (isMetadataInitialized()) return metadata.getExpiration();
        return expiration;
    }

    @Override
    public void setExpiration(Long expiration) {
        metadata.setExpiration(expiration);
    }

    @Override
    public UserSessionModel.State getState() {
        return metadata.getState();
    }

    @Override
    public void setState(UserSessionModel.State state) {
        metadata.setState(state);
    }

    @Override
    public UserSessionModel.SessionPersistenceState getPersistenceState() {
        return UserSessionModel.SessionPersistenceState.PERSISTENT;
    }

    @Override
    public void setPersistenceState(UserSessionModel.SessionPersistenceState persistenceState) {
        // no-op: each non-transient user session (stored in the db) has PERSISTENT state
    }

    @Override
    public Map<String, String> getNotes() {
        return Collections.unmodifiableMap(notes.stream().collect(Collectors.toMap(JpaUserSessionNoteEntity::getName, JpaUserSessionNoteEntity::getValue)));
    }

    @Override
    public String getNote(String name) {
        return notes.stream()
                .filter(obj -> Objects.equals(obj.getName(), name))
                .findFirst()
                .map(JpaUserSessionNoteEntity::getValue)
                .orElse(null);
    }

    @Override
    public void setNotes(Map<String, String> notes) {
        this.notes.clear();
        if (notes == null) return;
        for (Map.Entry<String, String> entry : notes.entrySet()) {
            setNote(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Boolean removeNote(String name) {
        return notes.removeIf(obj -> Objects.equals(obj.getName(), name));
    }

    @Override
    public void setNote(String name, String value) {
        removeNote(name);
        if (name == null || value == null || value.trim().isEmpty()) return;
        notes.add(new JpaUserSessionNoteEntity(this, name, value));
    }

    @Override
    public String getBrokerSessionId() {
        if (isMetadataInitialized()) return metadata.getBrokerSessionId();
        return brokerSessionId;
    }

    @Override
    public void setBrokerSessionId(String brokerSessionId) {
        metadata.setBrokerSessionId(brokerSessionId);
    }

    @Override
    public String getBrokerUserId() {
        if (isMetadataInitialized()) return metadata.getBrokerUserId();
        return brokerUserId;
    }

    @Override
    public void setBrokerUserId(String brokerUserId) {
        metadata.setBrokerUserId(brokerUserId);
    }

    @Override
    public Set<MapAuthenticatedClientSessionEntity> getAuthenticatedClientSessions() {
        return clientSessions.stream().map(MapAuthenticatedClientSessionEntity.class::cast).collect(Collectors.toSet());
    }

    @Override
    public void addAuthenticatedClientSession(MapAuthenticatedClientSessionEntity clientSession) {
        JpaClientSessionEntity jpaClientSession = JpaClientSessionEntity.class.cast(CLONER.from(clientSession));
        jpaClientSession.setParent(this);
        jpaClientSession.setEntityVersion(this.getEntityVersion());
        clientSessions.add(jpaClientSession);
    }

    @Override
    public Optional<MapAuthenticatedClientSessionEntity> getAuthenticatedClientSession(String clientUUID) {
        return clientSessions.stream().filter(cs -> Objects.equals(cs.getClientId(), clientUUID)).findFirst().map(MapAuthenticatedClientSessionEntity.class::cast);
    }

    @Override
    public Boolean removeAuthenticatedClientSession(String clientUUID) {
        return clientSessions.removeIf(cs -> Objects.equals(cs.getClientId(), clientUUID));
    }

    @Override
    public void clearAuthenticatedClientSessions() {
        clientSessions.clear();
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof JpaUserSessionEntity)) return false;
        return Objects.equals(getId(), ((JpaUserSessionEntity) obj).getId());
    }
}
