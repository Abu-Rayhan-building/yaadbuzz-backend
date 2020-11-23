import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './topic-rating.reducer';
import { ITopicRating } from 'app/shared/model/topic-rating.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITopicRatingDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const TopicRatingDetail = (props: ITopicRatingDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { topicRatingEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="yaadmaanApp.topicRating.detail.title">TopicRating</Translate> [<b>{topicRatingEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="repetitions">
              <Translate contentKey="yaadmaanApp.topicRating.repetitions">Repetitions</Translate>
            </span>
          </dt>
          <dd>{topicRatingEntity.repetitions}</dd>
          <dt>
            <Translate contentKey="yaadmaanApp.topicRating.rating">Rating</Translate>
          </dt>
          <dd>{topicRatingEntity.ratingId ? topicRatingEntity.ratingId : ''}</dd>
          <dt>
            <Translate contentKey="yaadmaanApp.topicRating.user">User</Translate>
          </dt>
          <dd>{topicRatingEntity.userId ? topicRatingEntity.userId : ''}</dd>
        </dl>
        <Button tag={Link} to="/topic-rating" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/topic-rating/${topicRatingEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ topicRating }: IRootState) => ({
  topicRatingEntity: topicRating.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(TopicRatingDetail);
