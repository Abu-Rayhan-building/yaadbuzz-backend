import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './charateristics-repetation.reducer';
import { ICharateristicsRepetation } from 'app/shared/model/charateristics-repetation.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICharateristicsRepetationDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CharateristicsRepetationDetail = (props: ICharateristicsRepetationDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { charateristicsRepetationEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="yaadmaanApp.charateristicsRepetation.detail.title">CharateristicsRepetation</Translate> [
          <b>{charateristicsRepetationEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="repetation">
              <Translate contentKey="yaadmaanApp.charateristicsRepetation.repetation">Repetation</Translate>
            </span>
          </dt>
          <dd>{charateristicsRepetationEntity.repetation}</dd>
          <dt>
            <Translate contentKey="yaadmaanApp.charateristicsRepetation.user">User</Translate>
          </dt>
          <dd>{charateristicsRepetationEntity.userId ? charateristicsRepetationEntity.userId : ''}</dd>
          <dt>
            <Translate contentKey="yaadmaanApp.charateristicsRepetation.charactristic">Charactristic</Translate>
          </dt>
          <dd>{charateristicsRepetationEntity.charactristicId ? charateristicsRepetationEntity.charactristicId : ''}</dd>
        </dl>
        <Button tag={Link} to="/charateristics-repetation" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/charateristics-repetation/${charateristicsRepetationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ charateristicsRepetation }: IRootState) => ({
  charateristicsRepetationEntity: charateristicsRepetation.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CharateristicsRepetationDetail);
