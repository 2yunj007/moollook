"use client";

import {useState} from "react";
import DetailProductDetail from "@/app/product/_components/DetailProductDetail";
import DetailReview from "@/app/product/_components/DetailReview";
import styles from "./detailProductInformation.module.css";
export default function DetailProductInformation() {
	const [isDetail, setIsDetail] = useState(true);

	const information = () => {
		if (isDetail == true) {
			return <DetailProductDetail />
		} else {
			return <DetailReview />
		}
	}

	return (
		<div className={styles.detailProductInformation__container}>
			<div className={styles.detailProductInformation__topDiv}>
				<button style={{color: isDetail ? "black" : "#333", backgroundColor: isDetail ? "white" : ""}} className={styles.detailProductInformation__button} onClick={() => setIsDetail(true)}>상세정보</button>
				<button style={{color: !isDetail ? "black" : "#333", backgroundColor: !isDetail ? "white" : ""}} className={styles.detailProductInformation__button} onClick={() => setIsDetail(false)}>리뷰</button>
			</div>
			{information()}
		</div>
	);
}