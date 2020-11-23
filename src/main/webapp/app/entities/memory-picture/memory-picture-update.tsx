import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, setFileData, openFile, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IMemory } from 'app/shared/model/memory.model';
import { getEntities as getMemories } from 'app/entities/memory/memory.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './memory-picture.reducer';
import { IMemoryPicture } from 'app/shared/model/memory-picture.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IMemoryPictureUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const MemoryPictureUpdate = (props: IMemoryPictureUpdateProps) => {
  const [memoryId, setMemoryId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { memoryPictureEntity, memories, loading, updating } = props;

  const { image, imageContentType } = memoryPictureEntity;

  const handleClose = () => {
    props.history.push('/memory-picture');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getMemories();
  }, []);

  const onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => props.setBlob(name, data, contentType), isAnImage);
  };

  const clearBlob = name => () => {
    props.setBlob(name, undefined, undefined);
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...memoryPictureEntity,
        ...values,
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="yaadmaanApp.memoryPicture.home.createOrEditLabel">
            <Translate contentKey="yaadmaanApp.memoryPicture.home.createOrEditLabel">Create or edit a MemoryPicture</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : memoryPictureEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="memory-picture-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="memory-picture-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <AvGroup>
                  <Label id="imageLabel" for="image">
                    <Translate contentKey="yaadmaanApp.memoryPicture.image">Image</Translate>
                  </Label>
                  <br />
                  {image ? (
                    <div>
                      {imageContentType ? (
                        <a onClick={openFile(imageContentType, image)}>
                          <img src={`data:${imageContentType};base64,${image}`} style={{ maxHeight: '100px' }} />
                        </a>
                      ) : null}
                      <br />
                      <Row>
                        <Col md="11">
                          <span>
                            {imageContentType}, {byteSize(image)}
                          </span>
                        </Col>
                        <Col md="1">
                          <Button color="danger" onClick={clearBlob('image')}>
                            <FontAwesomeIcon icon="times-circle" />
                          </Button>
                        </Col>
                      </Row>
                    </div>
                  ) : null}
                  <input id="file_image" type="file" onChange={onBlobChange(true, 'image')} accept="image/*" />
                  <AvInput
                    type="hidden"
                    name="image"
                    value={image}
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                    }}
                  />
                </AvGroup>
              </AvGroup>
              <AvGroup>
                <Label for="memory-picture-memory">
                  <Translate contentKey="yaadmaanApp.memoryPicture.memory">Memory</Translate>
                </Label>
                <AvInput id="memory-picture-memory" type="select" className="form-control" name="memoryId" required>
                  {memories
                    ? memories.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
                <AvFeedback>
                  <Translate contentKey="entity.validation.required">This field is required.</Translate>
                </AvFeedback>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/memory-picture" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  memories: storeState.memory.entities,
  memoryPictureEntity: storeState.memoryPicture.entity,
  loading: storeState.memoryPicture.loading,
  updating: storeState.memoryPicture.updating,
  updateSuccess: storeState.memoryPicture.updateSuccess,
});

const mapDispatchToProps = {
  getMemories,
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MemoryPictureUpdate);
