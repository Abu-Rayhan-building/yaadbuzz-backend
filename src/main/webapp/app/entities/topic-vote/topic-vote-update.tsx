import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ITopic } from 'app/shared/model/topic.model';
import { getEntities as getTopics } from 'app/entities/topic/topic.reducer';
import { IUserPerDepartment } from 'app/shared/model/user-per-department.model';
import { getEntities as getUserPerDepartments } from 'app/entities/user-per-department/user-per-department.reducer';
import { getEntity, updateEntity, createEntity, reset } from './topic-vote.reducer';
import { ITopicVote } from 'app/shared/model/topic-vote.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ITopicVoteUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const TopicVoteUpdate = (props: ITopicVoteUpdateProps) => {
  const [topicId, setTopicId] = useState('0');
  const [userId, setUserId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { topicVoteEntity, topics, userPerDepartments, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/topic-vote');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }

    props.getTopics();
    props.getUserPerDepartments();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...topicVoteEntity,
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
          <h2 id="yaadbuzzApp.topicVote.home.createOrEditLabel">
            <Translate contentKey="yaadbuzzApp.topicVote.home.createOrEditLabel">Create or edit a TopicVote</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : topicVoteEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="topic-vote-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="topic-vote-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="repetitionsLabel" for="topic-vote-repetitions">
                  <Translate contentKey="yaadbuzzApp.topicVote.repetitions">Repetitions</Translate>
                </Label>
                <AvField id="topic-vote-repetitions" type="string" className="form-control" name="repetitions" />
              </AvGroup>
              <AvGroup>
                <Label for="topic-vote-topic">
                  <Translate contentKey="yaadbuzzApp.topicVote.topic">Topic</Translate>
                </Label>
                <AvInput id="topic-vote-topic" type="select" className="form-control" name="topicId" required>
                  {topics
                    ? topics.map(otherEntity => (
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
                <Label for="topic-vote-user">
                  <Translate contentKey="yaadbuzzApp.topicVote.user">User</Translate>
                </Label>
                <AvInput id="topic-vote-user" type="select" className="form-control" name="userId" required>
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
              <Button tag={Link} id="cancel-save" to="/topic-vote" replace color="info">
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
  topics: storeState.topic.entities,
  userPerDepartments: storeState.userPerDepartment.entities,
  topicVoteEntity: storeState.topicVote.entity,
  loading: storeState.topicVote.loading,
  updating: storeState.topicVote.updating,
  updateSuccess: storeState.topicVote.updateSuccess,
});

const mapDispatchToProps = {
  getTopics,
  getUserPerDepartments,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(TopicVoteUpdate);
