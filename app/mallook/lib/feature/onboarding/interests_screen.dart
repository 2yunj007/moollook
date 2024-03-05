import 'package:flutter/material.dart';
import 'package:mallook/constants/gaps.dart';
import 'package:mallook/constants/sizes.dart';
import 'package:mallook/feature/onboarding/tutorial_screen.dart';
import 'package:mallook/feature/onboarding/widgets/interest_button.dart';

const interests = [
  "트렌디한",
  "클래식한",
  "빈티지한",
  "스트리트 스타일",
  "모던한",
  "캐주얼한",
  "우아한",
  "여성스러운",
  "남성적인",
  "보헤미안",
  "편안한",
  "화려한",
  "소피스티케이티드한",
  "엣지 있는",
  "페미닌한",
  "마스퀘라인",
  "차분한",
  "사파리 스타일",
  "레트로한",
  "아티스틱한",
  "새벽 섹시함",
  "프레피 스타일",
  "테일러 메이드",
  "스포티한",
  "도시적인",
  "고급스러운",
  "자연주의",
  "힙스터 스타일",
  "럭셔리한",
  "부드러운"
];

class InterestsScreen extends StatefulWidget {
  const InterestsScreen({super.key});

  @override
  State<InterestsScreen> createState() => _InterestsScreenState();
}

class _InterestsScreenState extends State<InterestsScreen> {
  final ScrollController _scrollController = ScrollController();
  bool _showTitle = false;

  void _onScroll() {
    if (_scrollController.offset >= 120) {
      if (_showTitle) return;
      setState(() {
        _showTitle = true;
      });
    } else {
      setState(() {
        _showTitle = false;
      });
    }
  }

  @override
  void initState() {
    super.initState();
    _scrollController.addListener(_onScroll);
  }

  @override
  void dispose() {
    _scrollController.dispose();
    super.dispose();
  }

  void _onNextTap() {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => const TutorialScreen(),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: AnimatedOpacity(
          opacity: _showTitle ? 1.0 : 0.0,
          duration: const Duration(milliseconds: 300),
          child: const Text('당신의 옷 취향은?'),
        ),
      ),
      body: Scrollbar(
        controller: _scrollController,
        child: SingleChildScrollView(
          controller: _scrollController,
          child: Padding(
            padding: const EdgeInsets.only(
              left: Sizes.size24,
              right: Sizes.size24,
              bottom: Sizes.size16,
            ),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Gaps.v24,
                Text(
                  '당신의 옷 스타일은?',
                  style: TextStyle(
                    color: Theme.of(context).colorScheme.primary,
                    fontSize: Sizes.size40,
                    fontWeight: FontWeight.bold,
                    height: 1.2,
                  ),
                ),
                Gaps.v16,
                Text(
                  '당신의 스타일로 추천 받아봐요!',
                  style: TextStyle(
                    color: Theme.of(context).colorScheme.secondary,
                    fontSize: Sizes.size20,
                  ),
                ),
                Gaps.v44,
                Wrap(
                  spacing: 15,
                  runSpacing: 15,
                  children: [
                    for (var interest in interests)
                      InterestButton(interest: interest)
                  ],
                ),
              ],
            ),
          ),
        ),
      ),
      bottomNavigationBar: BottomAppBar(
        height: Sizes.size96 + Sizes.size32,
        color: Theme.of(context).colorScheme.background,
        shadowColor: Theme.of(context).colorScheme.background,
        surfaceTintColor: Theme.of(context).colorScheme.background,
        elevation: 5,
        child: Padding(
          padding: const EdgeInsets.only(
            bottom: Sizes.size32,
            top: Sizes.size16,
            left: Sizes.size24,
            right: Sizes.size24,
          ),
          // MEMO: Another option: CupertinoButton
          child: GestureDetector(
            onTap: _onNextTap,
            child: Container(
              alignment: Alignment.center,
              padding: const EdgeInsets.symmetric(
                vertical: Sizes.size16,
              ),
              decoration: BoxDecoration(
                color: Theme.of(context)
                    .colorScheme
                    .onPrimaryContainer
                    .withOpacity(0.7),
                borderRadius: BorderRadius.circular(
                  Sizes.size12,
                ),
              ),
              child: Text(
                'Next',
                textAlign: TextAlign.center,
                style: TextStyle(
                  color: Theme.of(context).colorScheme.onPrimary,
                  fontSize: Sizes.size18,
                  fontWeight: FontWeight.w500,
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}
