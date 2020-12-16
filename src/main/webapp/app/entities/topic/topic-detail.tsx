import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './topic.reducer';
import { ITopic } from 'app/shared/model/topic.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITopicDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const TopicDetail = (props: ITopicDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { topicEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="yaadbuzzApp.topic.detail.title">Topic</Translate> [<b>{topicEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="title">
              <Translate contentKey="yaadbuzzApp.topic.title">Title</Translate>
            </span>
          </dt>
          <dd>{topicEntity.title}</dd>
          <dt>
            <Translate contentKey="yaadbuzzApp.topic.department">Department</Translate>
          </dt>
          <dd>{topicEntity.departmentId ? topicEntity.departmentId : ''}</dd>
          <dt>
            <Translate contentKey="yaadbuzzApp.topic.voters">Voters</Translate>
          </dt>
          <dd>
            {topicEntity.voters
              ? topicEntity.voters.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {topicEntity.voters && i === topicEntity.voters.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/topic" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/topic/${topicEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ topic }: IRootState) => ({
  topicEntity: topic.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(TopicDetail);
