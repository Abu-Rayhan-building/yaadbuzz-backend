import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUserPerDepartment } from 'app/shared/model/user-per-department.model';
import { getEntities as getUserPerDepartments } from 'app/entities/user-per-department/user-per-department.reducer';
import { IMemory } from 'app/shared/model/memory.model';
import { getEntities as getMemories } from 'app/entities/memory/memory.reducer';
import { getEntity, updateEntity, createEntity, reset } from './comment.reducer';
import { IComment } from 'app/shared/model/comment.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICommentUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CommentUpdate = (props: ICommentUpdateProps) => {
  const [writerId, setWriterId] = useState('0');
  const [memoryId, setMemoryId] = useState('0');
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { commentEntity, userPerDepartments, memories, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/comment');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }

    props.getUserPerDepartments();
    props.getMemories();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...commentEntity,
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
          <h2 id="yaadbuzzApp.comment.home.createOrEditLabel" data-cy="CommentCreateUpdateHeading">
            <Translate contentKey="yaadbuzzApp.comment.home.createOrEditLabel">Create or edit a Comment</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : commentEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="comment-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="comment-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="textLabel" for="comment-text">
                  <Translate contentKey="yaadbuzzApp.comment.text">Text</Translate>
                </Label>
                <AvField
                  id="comment-text"
                  data-cy="text"
                  type="text"
                  name="text"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="comment-writer">
                  <Translate contentKey="yaadbuzzApp.comment.writer">Writer</Translate>
                </Label>
                <AvInput id="comment-writer" data-cy="writer" type="select" className="form-control" name="writerId" required>
                  {userPerDepartments
                    ? userPerDepartments.map(otherEntity => (
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
              <AvGroup>
                <Label for="comment-memory">
                  <Translate contentKey="yaadbuzzApp.comment.memory">Memory</Translate>
                </Label>
                <AvInput id="comment-memory" data-cy="memory" type="select" className="form-control" name="memoryId" required>
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
              <Button tag={Link} id="cancel-save" to="/comment" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
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
  userPerDepartments: storeState.userPerDepartment.entities,
  memories: storeState.memory.entities,
  commentEntity: storeState.comment.entity,
  loading: storeState.comment.loading,
  updating: storeState.comment.updating,
  updateSuccess: storeState.comment.updateSuccess,
});

const mapDispatchToProps = {
  getUserPerDepartments,
  getMemories,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CommentUpdate);
